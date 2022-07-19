package com.shop.controller;

import com.shop.config.SecurityConfig;
import com.shop.entity.Member;
import com.shop.service.PostsService;
import com.shop.session.SessionMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@WebMvcTest(controllers = PostsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}

)
@ActiveProfiles("test")
@WithMockUser(roles="USER")
class PostsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostsService postsService;


    protected MockHttpSession mockHttpSession;


    @BeforeEach
    public void setUp() {
        mockHttpSession = new MockHttpSession();
        Member member = Member.builder().id(1L).name("123").email("123@123").picture("그림").build();
        SessionMember sessionMember = new SessionMember(member);
        mockHttpSession.setAttribute("member", sessionMember);
    }

    @Test
    void 게시판불러오기() throws Exception {

        Mockito.when(postsService.pageList(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        mockMvc.perform(get("/posts")
                .param("page","0")
                .param("size", "10")
                .param("sort","id,desc")
                .session(mockHttpSession)
        ).andExpect(status().isOk());

    }

}