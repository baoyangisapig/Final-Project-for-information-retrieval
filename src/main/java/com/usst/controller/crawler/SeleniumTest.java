package com.usst.controller.crawler;

import com.mashape.unirest.http.exceptions.UnirestException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SeleniumTest {
  public static void main(String[] args) throws UnirestException, IOException {
    for(int i=0;i<=99;i++){
      pp(i);
    }
  }

  public static void pp(int page) throws IOException {
    int j=1;
    File writename = new File("C:\\Users\\DELL\\IdeaProjects\\SearchEngine\\src\\main\\java\\output"+page+".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
    writename.createNewFile(); // 创建新文件
    BufferedWriter out = new BufferedWriter(new FileWriter(writename));
/*    out.write("我会写入文件啦\r\n"); // \r\n即为换行
    out.flush(); // 把缓存区内容压入文件
    out.close(); // 最后记得关闭文件*/
    System.setProperty("webdriver.chrome.driver", "src/main/java/chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.metacritic.com/browse/movies/score/metascore/all/filtered?sort=desc&page="+page);
    String prev="//*[@id=\"main_content\"]/div[1]/div[2]/div[5]/div[1]/div[1]/table/tbody/tr[";
    String tail="]/td[2]/a";
    String tail1="]/td[1]/a/img";
    WebDriver driver1 = new ChromeDriver();
    for(int i=1;i<=80;i++){
      if(j>50)break;
      int i1=1+2*(i-1);
      String xpath1=prev+i1+tail;
      String xpath2=prev+i1+tail+"/h3";
      String xpath3=prev+i1+tail1;
      String url=driver.findElement(By.xpath(xpath1)).getAttribute("href");
      String title=driver.findElement(By.xpath(xpath2)).getText();
      String imgUrl=driver.findElement(By.xpath(xpath3)).getAttribute("src");
      driver1.get(url);
      String summary="";
      String metascore="";
      String userScore="";
      String director="";
      StringBuilder comment=new StringBuilder();
      try {
        summary=driver1.findElement(By.xpath("//*[@id=\"main_content\"]/div[1]/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[4]/span[2]/span")).getText();
        metascore=driver1.findElement(By.xpath("//*[@id=\"main_content\"]/div[1]/div[1]/div/table/tbody/tr/td[2]/div/table/tbody/tr/td[1]/div/div/div[2]/table/tbody/tr/td[2]/a/span")).getText();
        userScore=driver1.findElement(By.xpath("//*[@id=\"main_content\"]/div[1]/div[1]/div/table/tbody/tr/td[2]/div/table/tbody/tr/td[1]/div/div/div[3]/div/table/tbody/tr/td[2]/a/span")).getText();
        List<WebElement> elements=driver1.findElements(By.className("summary"));
        director=driver1.findElement(By.className("director")).findElements(By.tagName("span")).get(1).getText();
        for(int k=0;k<=elements.size()-1;k++){
          if (k==0) continue;
          else comment.append(modify(elements.get(k).getText())).append("&&");
        }
        }
      catch (Exception e){
        continue;
      }
      //*[@id="main_content"]/div[1]/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[3]/span[2]/span
      String sum="number: "+j+++"\n"+"title: "+title+"\n"+"director: "+director+"\n"+"image: "+imgUrl+"\n"+"summary: "+summary+"\n"+"metascore: "+metascore+"\n"+"userScore: "+userScore+"\n"+"comment: "+comment.toString()+"\n";
      out.write(sum);
      out.flush();
    }
    out.close();
    driver.close();
    driver1.close();
  }

  public static String modify(String comment){
    if(comment.endsWith("… Expand")){
      return comment.replaceAll("… Expand","").replaceAll("\n","").trim();
    }
    return comment;
  }





}
