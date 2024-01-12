package com.example.questionservice.service;


import com.example.questionservice.dto.QuestionDTO;
import com.example.questionservice.entity.Answer;
import com.example.questionservice.entity.Question;
import com.example.questionservice.entity.QuestionWrapper;
import com.example.questionservice.repo.QuestionRepo;
import com.example.questionservice.util.VarList;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private ModelMapper modelMapper;

    public String saveQuestion(QuestionDTO questionDto){
        if(questionRepo.existsById(questionDto.getId())){
            return VarList.RSP_DUPLICATED;
        }else{
            questionRepo.save(modelMapper.map(questionDto, Question.class));
            return VarList.RSP_SUCCESS;
        }
    }


    public List<QuestionDTO> getAllQuestions(){
        List<Question> questionList = questionRepo.findAll();

        return modelMapper.map(questionList, new TypeToken<ArrayList<QuestionDTO>>(){}.getType());
    }

    public List<QuestionDTO> getAllQuestionsByCateogry(String category) {
        List<Question> questionList =  questionRepo.findByCategory(category);
        return modelMapper.map(questionList, new TypeToken<ArrayList<QuestionDTO>>(){}.getType());
    }


    public String updateQuestion(QuestionDTO questionDto) {
        if(questionRepo.existsById(questionDto.getId())){
            questionRepo.save(modelMapper.map(questionDto, Question.class));
            return VarList.RSP_SUCCESS;
        }else{
            return  VarList.RSP_NO_DATA_FOUND;
        }
    }

    public String deleteQuestionById(Integer questionId) {
        if(questionRepo.existsById(questionId)){
            questionRepo.deleteById(questionId);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionRepo.findRandojmQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromIds(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();

        //First we get a list of Questions
        List<Question> questions = new ArrayList<>();

        for(Integer id: questionIds){
            questions.add(questionRepo.findById(id).get());
        }

        //copping the question to wrapper
        for(Question question: questions){
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());

            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Answer> responses) {

        int right = 0;

        for(Answer answer : responses){
            Question question = questionRepo.findById(answer.getId()).get();

            if(answer.getResponse().equals(question.getRightAnswer()))
                right++;
        }

        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
