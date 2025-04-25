package com.capstone.meetingmap;

import com.capstone.meetingmap.board.repository.BoardRepository;
import com.capstone.meetingmap.board.repository.BoardRepositoryImpl;
import com.capstone.meetingmap.category.repository.CategoryRepository;
import com.capstone.meetingmap.category.repository.CategoryRepositoryImpl;
import com.capstone.meetingmap.friendship.repository.FriendshipRepository;
import com.capstone.meetingmap.friendship.repository.FriendshipRepositoryImpl;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleDetailRepositoryImpl;
import com.capstone.meetingmap.schedule.repository.ScheduleRepository;
import com.capstone.meetingmap.schedule.repository.ScheduleRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public BoardRepository boardRepository() {
        return new BoardRepositoryImpl(em);
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return new CategoryRepositoryImpl(em);
    }

    //CommentRepository 자리

    @Bean
    public FriendshipRepository friendshipRepository() {
        return new FriendshipRepositoryImpl(em);
    }

    //GroupUserRepository 자리

    @Bean
    public ScheduleRepository scheduleRepository() {
        return new ScheduleRepositoryImpl(em);
    }

    @Bean
    public ScheduleDetailRepository scheduleDetailRepository() {
        return new ScheduleDetailRepositoryImpl(em);
    }
}
