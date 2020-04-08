package com.usst.controller.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javafx.util.Pair;

public class Modified_BM25 {
  final static double k1=1.4,b=1.8;
  public static Map<String,String> comment=new HashMap<>();
  public static Map<String,String>  summary=new HashMap<>();
  public static Map<String,String> title=new Hashtable<String, String>();
  public static Map<String,String> director=new Hashtable<String, String>();
  public static Map<String,String> image=new Hashtable<String, String>();
  public static Map<String,String> metascore=new Hashtable<String, String>();
  public static Map<String,String> userScore=new Hashtable<String, String>();
  public static Map<String,Map<String,Integer>> wordFreqForComment=new HashMap<String, Map<String, Integer>>();
  public static Map<String,Map<String,Integer>> wordFreqForSummary=new HashMap<String, Map<String, Integer>>();

  public static void main(String[] args) throws IOException {
    new Modified_BM25().initialize();
    Queue<Pair<String,Double>> queue=selectTopK("comedy",100,wordFreqForSummary,summary,6,6);
    System.out.println(queue.size());
    while (!queue.isEmpty()){
      System.out.println(queue.peek().getKey()+": "+queue.peek().getValue());
      queue.poll();
    }
  }

  public void initialize() throws IOException {
    for(int i=0;i<=99;i++){
      Map<String,Map<String,String>> maps=ParseXML.parse("C:\\Users\\DELL\\IdeaProjects\\pdp_git\\PDP_5\\SpringMvc1\\src\\main\\java\\com\\usst\\files\\output"+i+".txt",i);
      merge(maps.get("title"),title);
      merge(maps.get("director"),director);
      merge(maps.get("image"),image);
      merge(maps.get("metascore"),metascore);
      merge(maps.get("userScore"),userScore);
      merge(maps.get("comment"),comment);
      merge(maps.get("summary"),summary);
    }

    for(String key:comment.keySet()){
        wordFreqForComment.put(key,new HashMap<String, Integer>());
        Map<String,Integer> cur=wordFreqForComment.get(key);
        String[] words=format(comment.get(key));
        for(String word:words){
          if(!cur.containsKey(word)){
            cur.put(word,1);
          }
          else{
            cur.put(word,cur.get(word)+1);
          }
        }
    }
    for(String key:summary.keySet()){
      wordFreqForSummary.put(key,new HashMap<String, Integer>());
      Map<String,Integer> cur=wordFreqForSummary.get(key);
      String[] words=format(summary.get(key));
      for(String word:words){
        if(!cur.containsKey(word)){
          cur.put(word,1);
        }
        else{
          cur.put(word,cur.get(word)+1);
        }
      }
    }

  }

  public static Queue<Pair<String,Double>> selectTopK(String query, int k,Map<String,Map<String,Integer>> wordFreq,Map<String,String> index,double limit_metaScore,double limit_userScore){
    Queue<Pair<String,Double>> queue=new PriorityQueue<Pair<String, Double>>((a, b)-> (int) ((a.getValue()-b.getValue())*100));
    Queue<Pair<String,Double>> queue1=new PriorityQueue<Pair<String, Double>>((a, b)-> (int) ((a.getValue()-b.getValue())*100));
    Map<String,Double> weight=getWeight(query,wordFreq);
    for(String recordId:wordFreq.keySet()){

      queue.offer(new Pair<>(recordId,scoreOfDocument(weight,recordId,wordFreq,index)));
      if(queue.size()>k){
        queue.poll();
      }
    }

  for(Pair<String,Double> pair:queue){
    if (!(pair.getValue()==0.0)&&!scoreLimit(pair.getKey(),limit_metaScore,limit_userScore)){
    queue1.offer(pair);
    }
  }
    return queue1;
  }

  public static Queue<Pair<String,Double>> selectTopByName(String keyWord,int k,double metaScore,double userScore){
    String[] words=keyWord.toLowerCase().split("\\s+");
    Queue<Pair<String,Double>> queue=new PriorityQueue<Pair<String, Double>>((a, b)-> (int) ((a.getValue()-b.getValue())*100));
    for(String key:title.keySet()){
      if (Double.parseDouble(Modified_BM25.metascore.get(key))<metaScore||Double.parseDouble(Modified_BM25.userScore.get(key))<userScore){
        continue;
      }
      String name=title.get(key);
      Set<String> set=new HashSet<>();
      for(String word:name.toLowerCase().replaceAll("[^a-zA-Z]"," ").split("\\s+")){
        set.add(word);
      }
      double score=0.0;
      for(String word:words){
        if (set.contains(word))score++;
      }
      if (score!=0.0)
      queue.offer(new Pair<String, Double>(key,score));
      if (queue.size()>k){
        queue.poll();
      }
    }
    return queue;
  }

  public static boolean scoreLimit(String key,double limit_metaScore,double limit_userScore){
    if (key.equals("83-3")){
      System.out.println("111");
    }
    boolean res;
    try{
      res= Double.parseDouble(metascore.get(key))<limit_metaScore||
              Double.parseDouble(userScore.get(key))<limit_userScore;
    }
  catch (Exception e){
      return true;
  }
    return  res;
  }

  public static double scoreOfDocument(Map<String, Double> weights, String recordId, Map<String, Map<String, Integer>> wordFreq, Map<String, String> index){
    double score=0;
    for(String key:weights.keySet()){
      score+=weights.get(key)*Rqd(key,recordId,aveLength(index),wordFreq,index);
    }
    return score;
  }

  private static int aveLength(Map<String, String> index){
    int total=0;
    for(String key:index.keySet()){
      total+=index.get(key).length();
    }
    return total/index.keySet().size();
  }

  public void merge(Map<String,String> temp,Map<String,String> cur){
    for(String key:temp.keySet()){
      cur.put(key,temp.get(key));
    }
  }

  public static String[] format(String token) {
    String[] tokens=token.toLowerCase().replaceAll("[^a-zA-Z]","  ").split("\\s+");
    for(int i=0;i<=tokens.length-1;i++){
      tokens[i]=tokens[i].toLowerCase();
    }
    return tokens;
  }

  public static Map<String,Double> getWeight(String query,Map<String,Map<String,Integer>> wordFreq){
    Map<String,Double> weights=new HashMap<String, Double>();
    Set<String> set=new HashSet<String>();
    String[] strs=query.toLowerCase().replaceAll("[^a-zA-Z]","  ").split("\\s+");
    for(String str:strs){
      set.add(str);
    }
    for(String key:set){
      weights.put(key,Math.log10((wordFreq.size()-appearTimes(key,wordFreq)+0.5)/(appearTimes(key,wordFreq)+0.5)));
    }
    return weights;
  }

  public static int appearTimes(String word,Map<String,Map<String,Integer>> wordFreq){
    int res=0;
    for(String key:wordFreq.keySet()){
      if(wordFreq.get(key).containsKey(word)){
        res++;
      }
    }
    return res;
  }

  public static double Rqd(String word, String recordId, int averageLength, Map<String, Map<String, Integer>> wordFreq, Map<String, String> index){
    double K= k1*(1-b+b*index.get(recordId).length()/averageLength);
    double fi=wordFreq.get(recordId).getOrDefault(word,0);
    return fi*(k1+1)/(fi+K);
  }

}
