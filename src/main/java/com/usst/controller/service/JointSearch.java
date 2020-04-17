package com.usst.controller.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javafx.util.Pair;

import static com.usst.controller.service.Modified_BM25.comment;
import static com.usst.controller.service.Modified_BM25.getWeight;
import static com.usst.controller.service.Modified_BM25.scoreOfDocument;
import static com.usst.controller.service.Modified_BM25.summary;
import static com.usst.controller.service.Modified_BM25.title;
import static com.usst.controller.service.Modified_BM25.wordFreqForComment;
import static com.usst.controller.service.Modified_BM25.wordFreqForSummary;

public class JointSearch {

  final private int sd=1;
  static double k1=-1;
  private static Map<String,Double> totalScoreForSummary=new HashMap<>();
  private static Map<String,Double> totalScoreForComment=new HashMap<>();
  private static Map<String,Double>  totalScoreForName=new HashMap<>();

  public static void main(String[] args) throws IOException {
    new Modified_BM25().initialize();

    Queue<Pair<String,Double>> queue=new JointSearch().selectTopK("love peace",5,40,4);
    while (!queue.isEmpty()){
      System.out.println(queue.poll().getKey());
    }
  }
  public  Queue<Pair<String,Double>> selectTopK(String query,int k,double limit_metaScore,double limit_userScore){
    Queue<Pair<String,Double>> queue=new PriorityQueue<Pair<String, Double>>((a, b)-> (int) ((a.getValue()-b.getValue())*100));
    Queue<Pair<String,Double>> queue1=new PriorityQueue<Pair<String, Double>>((a, b)-> (int) ((a.getValue()-b.getValue())*100));
    Map<String,Double> weight1=getWeight(query,Modified_BM25.wordFreqForComment);
    Map<String,Double> weight2=getWeight(query, wordFreqForSummary);
    for(String recordId: title.keySet()){
      queue.offer(new Pair<>(recordId,combineScore(recordId,query,weight1,weight2)));
      if(queue.size()>k){
        queue.poll();
      }
    }
    for(Pair<String,Double> pair:queue){
      if (!(pair.getValue()==0.0)&&!Modified_BM25.scoreLimit(pair.getKey(),limit_metaScore,limit_userScore)){
        queue1.offer(pair);
      }
    }
    return queue1;
  }

  public double gaussianFunction(int x){
    int u=averageLength();
    return 1/(Math.pow(2*3.14,0.5)*sd)*Math.pow(2.71,-Math.pow(x-u,2)/(2*Math.pow(sd,2)));
  }

  public int averageLength(){
    int count=0;
    int total=0;
    for(String key:Modified_BM25.title.keySet()){
      count++;
      total+= title.get(key).replaceAll("[^a-zA-Z]"," ").split("\\s+").length;
    }
    return total/count;
  }

  public double combineScore(String recordId,String query, Map<String,Double> weight1, Map<String,Double> weight2){
    if (k1==-1)
    k1=gaussianFunction(query.replaceAll("[^a-zA-Z]"," ").split("\\s+").length);
    double k2=(1-k1)*0.6;
    double k3=(1-k1)*0.4;
    return k1*calScore(query,recordId)+k2*scoreOfDocument(weight2,recordId,wordFreqForSummary,summary)+k3*scoreOfDocument(weight1,recordId,wordFreqForComment,comment);
  }



  public double calScore(String query,String recordId){
    if (!totalScoreForName.containsKey(query)){
      double total=0;
      String[] words=query.toLowerCase().split("\\s+");
      Set<String> set=new HashSet<>();
      for(String word:words){
        set.add(word);
      }
      for(String key:Modified_BM25.title.keySet()){
      String name= title.get(key);
      for(String cur:name.replaceAll("[^a-zA-Z]"," ").split("\\s+")){
        if(set.contains(cur)){
          total++;
        }
      }
      }
      totalScoreForName.put(query,total);
    }
    int score=0;
    String name=Modified_BM25.title.get(recordId);
    String[] words=query.toLowerCase().split("\\s+");
    Set<String> set=new HashSet<>();
    for(String word:words){
      set.add(word);
    }
    for(String word:name.toLowerCase().replaceAll("[^a-zA-Z]"," ").split("\\s+")){
      if (set.contains(word))score++;
    }
    return totalScoreForName.get(query)==0.0?0.0:score/totalScoreForName.get(query);
  }

}
