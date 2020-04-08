package com.usst.controller;

import com.usst.controller.pojo.Film;
import com.usst.controller.service.JointSearch;
import com.usst.controller.service.Modified_BM25;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import javafx.util.Pair;

/**
 * Created by 包杨 on 2020/02/09.
 */
@Controller
@RequestMapping(value="/demo")

@SessionAttributes(value={"username"})
public class FirController {
    /**
     * method that used to login into the website.
     *
     * @param mv
     * @return
     */
    @RequestMapping("logindemo")
    public ModelAndView LoginDemo(ModelAndView mv) throws IOException {
        new Modified_BM25().initialize();
        mv.setViewName("welcomepage/page");
        return mv;
    }


    /**
     * Search top 20 results.
     * @param
     * @param query
     * @return
     */
    @ResponseBody
    @RequestMapping("searchTop")
    public String SearchTop(@RequestParam(value="query")String query,@RequestParam(value="type") String type,@RequestParam("metaScore") String metaScore,@RequestParam("userScore")String userScore)  {
        Queue<Pair<String,Double>> queue=null;
        if (type.equals("1")){
            if (metaScore.equals("")||userScore.equals(" ")) {
            queue=Modified_BM25.selectTopByName(query,6,40,4);
            }
            else
                queue=Modified_BM25.selectTopByName(query,6,Double.parseDouble(metaScore),Double.parseDouble(userScore));
        }
        else if (type.equals("2")){
            if (metaScore.equals("")||userScore.equals(" ")){
                queue=Modified_BM25.selectTopK(query,6,Modified_BM25.wordFreqForComment,Modified_BM25.comment,40,4);
            }
           else
            queue=Modified_BM25.selectTopK(query,6,Modified_BM25.wordFreqForComment,Modified_BM25.comment,Double.parseDouble(metaScore),Double.parseDouble(userScore));
        }
        else if(type.equals("3")){
            if (metaScore.equals("")||userScore.equals(" ")){
                queue=Modified_BM25.selectTopK(query,6,Modified_BM25.wordFreqForSummary,Modified_BM25.summary,40,4);
            }
            else
            queue=Modified_BM25.selectTopK(query,6,Modified_BM25.wordFreqForSummary,Modified_BM25.summary,Double.parseDouble(metaScore),Double.parseDouble(userScore));

        }
        else if (type.equals("4")){
            if (metaScore.equals("")||userScore.equals(" ")){
                queue= new JointSearch().selectTopK(query,6,40,4);
            }
            else
                queue= new JointSearch().selectTopK(query,6,Double.parseDouble(metaScore),Double.parseDouble(userScore));
        }

        List<Film> list=new ArrayList<>();
        while (!queue.isEmpty()){
            Film film=new Film();
            String key=queue.poll().getKey();
            film.setDirector(Modified_BM25.director.get(key));
            film.setTitle(Modified_BM25.title.get(key));
            film.setImage(Modified_BM25.image.get(key));
            film.setMetaScore(Double.parseDouble(Modified_BM25.metascore.get(key)));
            film.setUserScore(Double.parseDouble(Modified_BM25.userScore.get(key)));
            film.setComment(Modified_BM25.comment.get(key));
            film.setSummary(Modified_BM25.summary.get(key));
            list.add(film);
        }
     Collections.reverse(list);
        return new JSONArray(list).toString();
    }
}
