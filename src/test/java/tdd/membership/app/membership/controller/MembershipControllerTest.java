package tdd.membership.app.membership.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tdd.membership.app.membership.controller.MembershipController;
import tdd.membership.app.membership.dto.MembershipDetailResponse;
import tdd.membership.app.membership.dto.MembershipRequest;
import tdd.membership.app.membership.dto.MembershipAddResponse;
import tdd.membership.exception.GlobalExceptionHandler;
import tdd.membership.exception.MembershipErrorResult;
import tdd.membership.exception.MembershipException;
import tdd.membership.app.membership.model.Membership;
import tdd.membership.app.membership.model.MembershipType;
import tdd.membership.app.membership.service.MembershipService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tdd.membership.app.membership.constants.MembershipConstants.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void mockMvc가Null이아님() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_포인트가null() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_포인트가음수() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_멤버십종류가Null() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    public void 멤버십등록실패_잘못된파라미터(final Integer point, final MembershipType membershipType) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_MemberService에서에러Throw() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = new MembershipAddResponse(-1L, MembershipType.NAVER);

        doReturn(membershipAddResponse)
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    public void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십목록조회성공() throws Exception {
        // gien
        final String url = "/api/v1/memberships";
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipService).getMembershipList("12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 멤버십상세조회실패_사용자식별자값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url, -1)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십상세조회실패_멤버십이존재하지않음() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
                .when(membershipService).getMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url, -1L)
                        .header(USER_ID_HEADER, "12345")
        );

        // when
        resultActions.andExpect(status().isNotFound());
    }
    @Test
    public void 멤버십상세조회실패_본인이아님() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        doThrow(new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER))
                .when(membershipService).getMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url, -1L)
                        .header(USER_ID_HEADER, "12345")
        );

        // when
        resultActions.andExpect(status().isBadRequest());
    }


    @Test
    public void 멤버십상세조회성공() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        final MembershipDetailResponse membershipAddResponse =
                new MembershipDetailResponse(-1L, MembershipType.NAVER, 10000, LocalDateTime.now());
        doReturn(membershipAddResponse).when(membershipService).getMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url, -1)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 멤버십삭제실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url, -1)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십삭제실패_멤버십이존재하지않음() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        doThrow(new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER))
                .when(membershipService).removeMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url, -1)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십삭제실패_본인이아님() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        doThrow(new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER))
                .when(membershipService).removeMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url, -1)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십삭제성공() throws Exception {
        // given
        final String url = "/api/v1/memberships/{id}";
        doNothing().when(membershipService).removeMembership(-1L, "12345");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url, -1)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isNoContent());
    }





    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.createMembershipRequest(point, membershipType);
    }



    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
                Arguments.of(null, MembershipType.NAVER),
                Arguments.of(-1, MembershipType.NAVER),
                Arguments.of(10000, null)
        );
    }
}
