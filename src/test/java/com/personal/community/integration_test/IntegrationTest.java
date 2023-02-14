package com.personal.community.integration_test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.community.config.RestDocsConfig;
import com.personal.community.domain.post.repository.CommentRepository;
import com.personal.community.domain.post.repository.PostRepository;
import com.personal.community.domain.post.repository.ViewRepository;
import com.personal.community.domain.user.dto.RequestUserDto.UserSigninDto;
import com.personal.community.domain.user.dto.ResponseUserDto.SigninUserDto;
import com.personal.community.domain.user.entity.User;
import com.personal.community.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@ActiveProfiles("test")
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class IntegrationTest {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    protected ObjectMapper objectMapper;
    protected MockMvc mvc;
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected ViewRepository viewRepository;
    @Autowired
    protected CommentRepository commentRepository;

    protected String userBaseUrl = "/api/v1/users";
    protected String postBaseUrl = "/api/v1/posts";
    protected Logger log = LoggerFactory.getLogger(Logger.class);

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    public String getToken(String email, String password) throws Exception{
        UserSigninDto userSigninDto = new UserSigninDto();
        userSigninDto.setEmail(email);
        userSigninDto.setPassword(password);

        ResultActions result = mvc.perform(post("/api/v1/users/signin")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(userSigninDto)));

        String response = result.andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        SigninUserDto signinUserDto = objectMapper.readValue(response, SigninUserDto.class);
        return signinUserDto.getToken().getAccessToken();
    }


    public User createUserForTest(String email, String password) {
        return User.ofCreate(email, passwordEncoder.encode(password), "nickname");
    }

    protected User saveUser(User user) {
        return userRepository.save(user);
    }


    protected void deleteAllRepository() {
        commentRepository.deleteAll();
        viewRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }
}
