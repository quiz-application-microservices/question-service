package com.example.questionservice.controllers;


import com.example.questionservice.dto.QuestionDTO;
import com.example.questionservice.dto.ResponseDTO;
import com.example.questionservice.entity.Answer;
import com.example.questionservice.entity.QuestionWrapper;
import com.example.questionservice.service.QuestionService;
import com.example.questionservice.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private ResponseDTO responseDTO;

    //Create a Question
    @PostMapping(value = "/saveQuestion") // api/v1/question/saveQuestion
    public ResponseEntity saveQuestion(@RequestBody QuestionDTO questionDto){
        try{
            String res = questionService.saveQuestion(questionDto);

            if(res.equals("00")){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("success");
                responseDTO.setContent(questionDto);

                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            }else{
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("bad request");
                responseDTO.setContent(null);

                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("bad request");
            responseDTO.setContent(null);

            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    //Get all the questions
    @GetMapping(value = "/getAllQuestions")
    public ResponseEntity getAllQuestions(){
        try {
            List<QuestionDTO> questioinDtoList = questionService.getAllQuestions();

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("success");
            responseDTO.setContent(questioinDtoList);

            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
        }catch (Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("bad request");
            responseDTO.setContent(null);

            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    //Get question by category
    @GetMapping(value="/getAllQuestionsByCategory/category/{category}")
    public ResponseEntity getQuestionsByCategory(@PathVariable String category){
        try{
            List<QuestionDTO> questionDTOList = questionService.getAllQuestionsByCateogry(category);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("success");
            responseDTO.setContent(questionDTOList);
            return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("error");
            responseDTO.setContent(null);
            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    //update a question
    @PutMapping(value = "/updateQuestion")
    public ResponseEntity updateQuestion(@RequestBody QuestionDTO questionDto){
        try{
            String res = questionService.updateQuestion(questionDto);

            if(res.equals("00")){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("success");
                responseDTO.setContent(questionDto);

                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            }else{
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("error");
                responseDTO.setContent(null);

                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("error");
            responseDTO.setContent(null);

            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    //delete a question
    @DeleteMapping(value="/deleteQuestion/{questionId}")
    public ResponseEntity deleteQuestion(@PathVariable Integer questionId){
        try{

            String res = questionService.deleteQuestionById(questionId);

            if(res.equals("00")){
                responseDTO.setCode(VarList.RSP_SUCCESS);
                responseDTO.setMessage("success");
                responseDTO.setContent(null);

                return new ResponseEntity(responseDTO, HttpStatus.ACCEPTED);
            }else{
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("error");
                responseDTO.setContent(null);

                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("error");
            responseDTO.setContent(null);

            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }




    //methods that are quested by the quiz service
    //generate a quiz
    @GetMapping("/generateQuiz")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz
        (@RequestParam String categoryName, @RequestParam Integer numQuestions){
        return questionService.getQuestionsForQuiz(categoryName, numQuestions);
    }

    //get questions
    @PostMapping("/getQuestionsFromIds")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds){
        return questionService.getQuestionsFromIds(questionIds);
    }

    //calculate the score
    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Answer> responses){
        return questionService.getScore(responses);
    }
}



