package com.usst.controller.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ParseXML {
  public static void main(String[] args) throws IOException {

    }

    public static Map<String, Map<String,String>> parse(String path,int index) throws IOException {
      Map<String, Map<String,String>> resMap=new HashMap<>();
      resMap.put("title",new HashMap<>());
      resMap.put("director",new HashMap<>());
      resMap.put("image",new HashMap<>());
      resMap.put("summary",new HashMap<>());
      resMap.put("metascore",new HashMap<>());
      resMap.put("userScore",new HashMap<>());
      resMap.put("comment",new HashMap<>());
      FileInputStream inputStream = new FileInputStream(path);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String str=null;
      String curNumber="";
      while ((str = bufferedReader.readLine()) != null) {
        if (str.startsWith("number")){
          curNumber=index+"-"+str.split(" ")[1];
        }
        else if (str.startsWith("title")){
          resMap.get("title").put(curNumber,str.replace("title: ",""));
        }
        else if (str.startsWith("director")){
          resMap.get("director").put(curNumber,str.replace("director: ",""));
        }
        else if (str.startsWith("image")){
          resMap.get("image").put(curNumber,str.replace("image: ",""));
        }
        else if (str.startsWith("summary")){
          resMap.get("summary").put(curNumber,str.replace("summary:","").replace("Expand",""));
        }
        else if (str.startsWith("metascore")){
          String metaScore=str.split(" ")[1];
          if (metaScore.equals("tbd")){
            metaScore="65";
          }
          resMap.get("metascore").put(curNumber,metaScore);
        }
        else if (str.startsWith("userScore")){
          String userScore=str.split(" ")[1];
          if (userScore.equals("tbd")){
            userScore="6.5";
          }
          resMap.get("userScore").put(curNumber,userScore);
        }
        else{
            String cur=resMap.get("comment").getOrDefault(curNumber,"");
            resMap.get("comment").put(curNumber,cur+" "+str);
          }
        }
      for(String key:resMap.get("comment").keySet()){
        String cur=resMap.get("comment").get(key).replace("comment: ","" ).replaceAll("&&","<HR align=center width=300 color=#987cb9 SIZE=1>").replaceAll(" Read full review","");
        resMap.get("comment").put(key,cur);
      }
      return resMap;
    }
  }

