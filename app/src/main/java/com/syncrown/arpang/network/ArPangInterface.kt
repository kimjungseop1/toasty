package com.syncrown.arpang.network

import com.syncrown.arpang.network.model.ResponseAddCommentDto
import com.syncrown.arpang.network.model.ResponseAllFavoriteDto
import com.syncrown.arpang.network.model.ResponseAppMainDto
import com.syncrown.arpang.network.model.ResponseAppMenuByCartridgeDto
import com.syncrown.arpang.network.model.ResponseBlockUserListDto
import com.syncrown.arpang.network.model.ResponseCartridgeByAppMenuDto
import com.syncrown.arpang.network.model.ResponseCartridgeListByTagDto
import com.syncrown.arpang.network.model.ResponseCartridgeListDto
import com.syncrown.arpang.network.model.ResponseCheckMember
import com.syncrown.arpang.network.model.ResponseCheckNickNameDto
import com.syncrown.arpang.network.model.ResponseCommentListDto
import com.syncrown.arpang.network.model.ResponseCommentReportDto
import com.syncrown.arpang.network.model.ResponseCommonListDto
import com.syncrown.arpang.network.model.ResponseContentReportDto
import com.syncrown.arpang.network.model.ResponseDelCommentDto
import com.syncrown.arpang.network.model.ResponseDeleteStorageDto
import com.syncrown.arpang.network.model.ResponseDetailContentHashTagDto
import com.syncrown.arpang.network.model.ResponseEditContentHashTagDto
import com.syncrown.arpang.network.model.ResponseFavoriteDto
import com.syncrown.arpang.network.model.ResponseIgnoreTagCheckDto
import com.syncrown.arpang.network.model.ResponseJoinDto
import com.syncrown.arpang.network.model.ResponseLoginDto
import com.syncrown.arpang.network.model.ResponseMainBannerDto
import com.syncrown.arpang.network.model.ResponseMultiCommonListDto
import com.syncrown.arpang.network.model.ResponseMyFavoriteDto
import com.syncrown.arpang.network.model.ResponseNoticeDetailDto
import com.syncrown.arpang.network.model.ResponseNoticeListDto
import com.syncrown.arpang.network.model.ResponsePopularTagDto
import com.syncrown.arpang.network.model.ResponsePublicContentSettingDto
import com.syncrown.arpang.network.model.ResponseRecommendTagListDto
import com.syncrown.arpang.network.model.ResponseResultMatchDto
import com.syncrown.arpang.network.model.ResponseSaveEditorDto
import com.syncrown.arpang.network.model.ResponseScrapContentDto
import com.syncrown.arpang.network.model.ResponseScrapUpdateDto
import com.syncrown.arpang.network.model.ResponseSearchingMatchDto
import com.syncrown.arpang.network.model.ResponseShareContentAllOpenListDto
import com.syncrown.arpang.network.model.ResponseShareContentUserOpenListDto
import com.syncrown.arpang.network.model.ResponseShareDetailDto
import com.syncrown.arpang.network.model.ResponseStorageContentListDto
import com.syncrown.arpang.network.model.ResponseStorageDetailDto
import com.syncrown.arpang.network.model.ResponseSubscribeListDto
import com.syncrown.arpang.network.model.ResponseSubscribeUpdateDto
import com.syncrown.arpang.network.model.ResponseSubscribeTotalDto
import com.syncrown.arpang.network.model.ResponseSubscribeUserContentListDto
import com.syncrown.arpang.network.model.ResponseTagsByCartridgeDto
import com.syncrown.arpang.network.model.ResponseTargetUserBlockDto
import com.syncrown.arpang.network.model.ResponseTargetUserReportDto
import com.syncrown.arpang.network.model.ResponseTemplateListDto
import com.syncrown.arpang.network.model.ResponseUpdateProfileDto
import com.syncrown.arpang.network.model.ResponseUserAlertListDto
import com.syncrown.arpang.network.model.ResponseUserAlertSettingDto
import com.syncrown.arpang.network.model.ResponseUserProfileDto
import com.syncrown.arpang.network.model.ResponseUserTokenDelDto
import com.syncrown.arpang.network.model.ResponseUserTokenRegDto
import com.syncrown.arpang.network.model.ResponseWithdrawalDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ArPangInterface {
    //TODO 001. 사용자 토큰 등록
    @FormUrlEncoded
    @POST("/ntv/insert/user/token")
    fun postInsertUserToken(
        @Field("user_token") user_token: String,
        @Field("app_id") app_id: String,
        @Field("device_id") device_id: String,
        @Field("user_id") user_id: String?
    ): Call<ResponseUserTokenRegDto>

    //TODO 002. 사용자 토큰 삭제
    @FormUrlEncoded
    @POST("/ntv/delete/user/token")
    fun postDeleteUserToken(
        @Field("app_id") app_id: String,
        @Field("device_id") device_id: String
    ): Call<ResponseUserTokenDelDto>

    //TODO 003. 회원여부 확인
    @FormUrlEncoded
    @POST("/ntv/member/check/app")
    fun postCheckMember(
        @Field("user_id") user_id: String
    ): Call<ResponseCheckMember>

    //TODO 004. 가입신청
    @FormUrlEncoded
    @POST("/ntv/join/req/app")
    fun postJoin(
        @Field("user_id") user_id: String,
        @Field("login_connt_accnt") login_connt_accnt: String,
        @Field("userid_se") userid_se: Int,
        @Field("user_nm") user_nm: String?,
        @Field("use_se") use_se: Int,
        @Field("mobl_no") mobl_no: String?,
        @Field("email") email: String?,
        @Field("nick_nm") nick_nm: String?,
        @Field("stt_msg") stt_msg: String?,
        @Field("nation") nation: String?,
        @Field("lang") lang: String?,
        @Field("birth_day") birth_day: String?,
        @Field("markt_recptn_agre") markt_recptn_agre: Int?,
        @Field("profile") profile: String?,
        @Field("app_id") app_id: String
    ): Call<ResponseJoinDto>

    //TODO 005. 로그인
    @FormUrlEncoded
    @POST("/ntv/login/app")
    fun postLogin(
        @Field("user_id") user_id: String
    ): Call<ResponseLoginDto>

    //TODO 006. 프로필 수정
    @FormUrlEncoded
    @POST("/ntv/atp/user/update")
    fun postUpdateProfile(
        @Field("user_id") user_id: String,
        @Field("nick_nm") nick_nm: String?,
        @Field("mobl_no") mobl_no: String?,
        @Field("stt_msg") stt_msg: String?,
        @Field("lang") lang: String?,
        @Field("profile") profile: String?
    ): Call<ResponseUpdateProfileDto>

    //TODO 006-1. 사용자 정보 조회
    @FormUrlEncoded
    @POST("/ntv/atp/user/get")
    fun postUserProfile(
        @Field("user_id") user_id: String
    ): Call<ResponseUserProfileDto>

    //TODO 007. 별명 중복 체크
    @FormUrlEncoded
    @POST("/ntv/atp/nick/duple")
    fun postCheckNickName(
        @Field("user_id") user_id: String,
        @Field("nick_nm") nick_nm: String
    ): Call<ResponseCheckNickNameDto>

    //TODO 008. 회원탈퇴
    @FormUrlEncoded
    @POST("/ntv/atp/member/break")
    fun postWithdrawal(
        @Field("user_id") user_id: String,
        @Field("break_reson") break_reson: String?,
        @Field("etc_reson") etc_reson: String?
    ): Call<ResponseWithdrawalDto>

    //TODO 009. 공통코드 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/common/combo")
    fun postCommonList(
        @Field("lcode") lcode: Int
    ): Call<ResponseCommonListDto>

    //TODO 009-1. 공통코드 리스트(코드값 복수개)
    @FormUrlEncoded
    @POST("/ntv/atp/list/common/combo/in")
    fun postMultiCommonList(
        @Field("lcode") lcode: Int,
        @Field("mcode") mcode: String?
    ): Call<ResponseMultiCommonListDto>

    //TODO 010. 앱메인메뉴 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/appmenu")
    fun postAppMain(
        @Field("app_id") app_id: String
    ): Call<ResponseAppMainDto>

    //TODO 011. 카트리지 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/list")
    fun postCartridgeList(
        @Field("app_id") app_id: String,
        @Field("device_os") device_os: Int,
        @Field("ctge_no") ctge_no: Int?,
        @Field("ctge_nm") ctge_nm: String?,
        @Field("ctge_model") ctge_model: String?,
        @Field("ctge_model_abbr") ctge_model_abbr: String?,
        @Field("character_brand") character_brand: String?
    ): Call<ResponseCartridgeListDto>

    //TODO 011-1 추천태그 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/tag/recommend/all")
    fun postRecommendAllList(
        @Field("app_id") app_id: String
    ): Call<ResponseRecommendTagListDto>

    //TODO 012. 카트리지별 앱메뉴 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/appmenu/list")
    fun postAppMenuByCartridge(
        @Field("ctge_no") ctge_no: Int,
        @Field("app_id") app_id: String
    ): Call<ResponseAppMenuByCartridgeDto>

    //TODO 013. 앱메뉴별 카트리지 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/appmenu/cartridge/list")
    fun postCartridgeByAppMenu(
        @Field("menu_code") menu_code: String,
        @Field("app_id") app_id: String
    ): Call<ResponseCartridgeByAppMenuDto>

    //TODO 014. 카트리지별 추천태그 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/cartridge/tag/recommend/list")
    fun postTagsByCartridge(
        @Field("ctge_no") ctge_no: Int,
        @Field("app_id") app_id: String
    ): Call<ResponseTagsByCartridgeDto>

    //TODO 015. 추천태그별 카트리지 리스트를 가져온다
    @FormUrlEncoded
    @POST("/ntv/atp/tag/recommend/cartridge/list")
    fun postCartridgeListByTags(
        @Field("tag_seq_no") tag_seq_no: String,
        @Field("app_id") app_id: String,
        @Field("menu_code") menu_code: String?,
        @Field("currPage") currPage: Int?,
        @Field("pageSize") pageSize: Int?
    ): Call<ResponseCartridgeListByTagDto>

    //TODO 016. 사용자 알림 설정
    @FormUrlEncoded
    @POST("/ntv/atp/upate/user/notify/setting")
    fun postUserAlertSetting(
        @Field("user_id") user_id: String,
        @Field("noti_event_se") noti_event_se: Int?,
        @Field("subscrip_se") subscrip_se: Int?,
        @Field("favor_se") favor_se: Int?,
        @Field("comment_se") comment_se: Int?
    ): Call<ResponseUserAlertSettingDto>

    //TODO 017. 사용자 알림 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/user/notify/setting")
    fun postUserAlertList(
        @Field("user_id") user_id: String
    ): Call<ResponseUserAlertListDto>

    //TODO 018. 템플릿 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/templete/list")
    fun postTemplateList(
        @Field("ctgy_se") ctgy_se: String,
        @Field("ctgy_code_step1") ctgy_code_step1: Int?,
        @Field("version_no_start") version_no_start: Int?,
        @Field("version_no_end") version_no_end: Int?,
        @Field("character_brand") character_brand: String?,
        @Field("currPage") currPage: Int?,
        @Field("pageSize") pageSize: Int?
    ): Call<ResponseTemplateListDto>

    //TODO 019. 금지해시태그조회
    @FormUrlEncoded
    @POST("/ntv/atp/hashtag/isprohibit")
    fun postIgnoreTagCheck(
        @Field("share_hash_tag") share_hash_tag: String
    ): Call<ResponseIgnoreTagCheckDto>

    //TODO 020. 에디터 Object 저장(일괄)
    @FormUrlEncoded
    @POST("/ntv/atp/insert/editor/total")
    fun postSaveEditor(
        @Field("user_id") user_id: String,
        @Field("ctge_no") ctge_no: Int,
        @Field("main_image") main_image: String,
        @Field("editor_data") editor_data: String?,
        @Field("pixel_x") pixel_x: Int?,
        @Field("share_hash_tag") share_hash_tag: String?,
        @Field("share_se") share_se: Int,
        @Field("menu_code") menu_code: Int,
        @Field("parent_cntnts_no") parent_cntnts_no: String?
    ): Call<ResponseSaveEditorDto>

    //TODO 021. 보관함(사용자) 저장 컨텐츠 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/contents/mysave")
    fun postStorageContentList(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int,
        @Field("hash_tag") hash_tag: String?,
        @Field("menu_code") menu_code: String?,
        @Field("ctge_no") ctge_no: String?,
        @Field("share_se") share_se: String?
    ): Call<ResponseStorageContentListDto>

    //TODO 022. 보관함(사용자) 저장 컨텐츠 상세조회
    @FormUrlEncoded
    @POST("/ntv/atp/detail/contents/mysave")
    fun postStorageContentDetail(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String
    ): Call<ResponseStorageDetailDto>

    //TODO 023. 사용자가 공유(공개)한 컨텐츠 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/contents/share")
    fun postShareContentUserOpenList(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int?,
        @Field("pageSize") pageSize: Int?,
        @Field("hash_tag") hash_tag: String?,
        @Field("menu_code") menu_code: Int?
    ): Call<ResponseShareContentUserOpenListDto>

    //TODO 024. 전체 공유(공개) 컨텐츠 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/contents/share/all")
    fun postShareContentAllOpenList(
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int,
        @Field("hash_tag") hash_tag: String?,
        @Field("menu_code") menu_code: String?
    ): Call<ResponseShareContentAllOpenListDto>

    //TODO 025. 공개(공유)된 컨텐츠 상세조회
    @FormUrlEncoded
    @POST("/ntv/atp/detail/contents/share")
    fun postShareDetailContent(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String
    ): Call<ResponseShareDetailDto>

    //TODO 026. 전체 인기있는 해시태그 리스트(상위5건)
    @FormUrlEncoded
    @POST("/ntv/atp/list/hashtag/all/favored")
    fun postAllFavoriteHashTag(
        @Field("search_nm") search_nm: String
    ): Call<ResponseAllFavoriteDto>

    //TODO 027. 내가등록한 해시태그 리스트 가져오기 (상위5건)
    @FormUrlEncoded
    @POST("/ntv/atp/list/hashtag/my/favored")
    fun postMyFavoriteHashTag(
        @Field("search_nm") search_nm: String
    ): Call<ResponseMyFavoriteDto>

    //TODO 028. 댓글 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/contents/comment")
    fun postCommentList(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int
    ): Call<ResponseCommentListDto>

    //TODO 029. 댓글 작성
    @FormUrlEncoded
    @POST("/ntv/atp/insert/contents/comment")
    fun postAddComment(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String,
        @Field("comment") comment: String
    ): Call<ResponseAddCommentDto>

    //TODO 030. 댓글 삭제
    @FormUrlEncoded
    @POST("/ntv/atp/delete/contents/comment")
    fun postDelComment(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String,
        @Field("seq_no") seq_no: String
    ): Call<ResponseDelCommentDto>

    //TODO 031. 상세컨텐츠의 해시태그 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/hashtag/contents")
    fun postDetailHashTagList(
        @Field("cntnts_no") cntnts_no: String
    ): Call<ResponseDetailContentHashTagDto>

    //TODO 032. 컨텐츠 공개여부 설정하기
    @FormUrlEncoded
    @POST("/ntv/atp/update/contents/share/se")
    fun postPublicContentSetting(
        @Field("cntnts_no") cntnts_no: String,
        @Field("share_se") share_se: String,
        @Field("menu_code") menu_code: String,
        @Field("user_id") user_id: String
    ): Call<ResponsePublicContentSettingDto>

    //TODO 033-1. 구독하기 등록
    @FormUrlEncoded
    @POST("/ntv/atp/update/subscription/my")
    fun postSubscribeReg(
        @Field("user_id") user_id: String,
        @Field("sub_user_id") sub_user_id: String,
        @Field("subscription_se") subscription_se: Int
    ): Call<ResponseSubscribeUpdateDto>

    //TODO 033-2. 내가 구독한 사용자 목록
    @FormUrlEncoded
    @POST("/ntv/atp/list/subscription/my")
    fun postSubscribeListByMy(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int
    ): Call<ResponseSubscribeListDto>

    //TODO 033-3. 나를 구독한 사용자 목록
    @FormUrlEncoded
    @POST("/ntv/atp/list/subscription/me")
    fun postSubscribeListByMe(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int
    ): Call<ResponseSubscribeListDto>

    //TODO 033-5. 구독자 전체 갯수 조회
    @FormUrlEncoded
    @POST("/ntv/atp/select/subscription/myme/cnt")
    fun postSubscribeTotalCount(
        @Field("user_id") user_id: String
    ): Call<ResponseSubscribeTotalDto>

    //TODO 034. 상세컨텐츠의 해시태그 리스트 편집
    @FormUrlEncoded
    @POST("/ntv/atp/update/hashtag/contents")
    fun postEditContentHashTag(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String,
        @Field("share_hash_tag") share_hash_tag: String
    ): Call<ResponseEditContentHashTagDto>

    //TODO 035. 보관함 컨텐츠 삭제
    @FormUrlEncoded
    @POST("/ntv/atp/delete/contents/mysave")
    fun postDeleteStorageContent(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String
    ): Call<ResponseDeleteStorageDto>

    //TODO 036. 댓글신고(다른사람이 쓴 댓글)
    @FormUrlEncoded
    @POST("/ntv/atp/insert/contents/comment/complain")
    fun postCommentReport(
        @Field("cntnts_no") cntnts_no: String,
        @Field("comment_seq_no") comment_seq_no: String,
        @Field("user_id") user_id: String,
        @Field("write_user_id") write_user_id: String,
        @Field("complain_desc") complain_desc: String?
    ): Call<ResponseCommentReportDto>

    //TODO 037. 구독한 사용자의 공유(공개)한 컨텐츠 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/subscription/user/contents/share")
    fun postSubscribeUserContentList(
        @Field("sub_user_id") sub_user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int,
        @Field("hash_tag") hash_tag: String?,
        @Field("menu_code") menu_code: String?
    ): Call<ResponseSubscribeUserContentListDto>

    //TODO 038. 좋아요 등록/해제
    @FormUrlEncoded
    @POST("/ntv/atp/update/contents/favorite")
    fun postFavoriteUpdate(
        @Field("user_id") user_id: String,
        @Field("cntnts_no") cntnts_no: String,
        @Field("favorite_se") favorite_se: Int
    ): Call<ResponseFavoriteDto>

    //TODO 039. 공유공간 상세 게시글을 신고
    @FormUrlEncoded
    @POST("/ntv/atp/insert/contents/complain")
    fun postContentReport(
        @Field("cntnts_no") cntnts_no: String,
        @Field("user_id") user_id: String,
        @Field("write_user_id") write_user_id: String,
        @Field("complain_desc") complain_desc: String?
    ): Call<ResponseContentReportDto>

    //TODO 040. 스크랩 등록/삭제
    @FormUrlEncoded
    @POST("/ntv/atp/update/contents/scrap")
    fun postScrapUpdate(
        @Field("user_id") user_id: String,
        @Field("cntnts_no") cntnts_no: String,
        @Field("scrap_se") scrap_se: Int
    ): Call<ResponseScrapUpdateDto>

    //TODO 041. 스크랩 컨텐츠 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/contents/scrap")
    fun postScrapContentList(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int,
        @Field("hash_tag") hash_tag: String?,
        @Field("menu_code") menu_code: String?,
        @Field("ctge_no") ctge_no: String?
    ): Call<ResponseScrapContentDto>

    //TODO 042. 특정 사용자 신고하기
    @FormUrlEncoded
    @POST("/ntv/atp/insert/user/complain")
    fun postTargetUserReport(
        @Field("user_id") user_id: String,
        @Field("complain_user_id") complain_user_id: String,
        @Field("complain_desc") complain_desc: String?
    ): Call<ResponseTargetUserReportDto>

    //TODO 043. 특정 사용자 차단 등록/해제
    @FormUrlEncoded
    @POST("/ntv/atp/update/user/block/my")
    fun postTargetUserBlock(
        @Field("user_id") user_id: String,
        @Field("block_user_id") block_user_id: String,
        @Field("block_se") block_se: Int
    ): Call<ResponseTargetUserBlockDto>

    //TODO 044. 차단한 사용자 목록
    @FormUrlEncoded
    @POST("/ntv/atp/list/user/block/my")
    fun postBlockUserList(
        @Field("user_id") user_id: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int
    ): Call<ResponseBlockUserListDto>

    //TODO 045. 공지사항 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/notice")
    fun postNoticeList(
        @Field("app_id") app_id: String,
        @Field("lang_code") lang_code: String?
    ): Call<ResponseNoticeListDto>

    //TODO 046. 공지사항 상세보기
    @FormUrlEncoded
    @POST("/ntv/atp/detail/notice")
    fun postNoticeDetail(
        @Field("bbsid") bbsid: String
    ): Call<ResponseNoticeDetailDto>

    //TODO 047. 인기 검색 태그
    @POST("/ntv/atp/list/search/popular/hashtag")
    fun postPopularTag(): Call<ResponsePopularTagDto>

    //TODO 048. 입력된 단어가 포함된 이름/공유/보관함 검색중 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/search/hashtag/all")
    fun postSearchingInputMatch(
        @Field("user_id") user_id: String,
        @Field("search_nm") search_nm: String
    ): Call<ResponseSearchingMatchDto>

    //TODO 049. 입력된 단어가 포함된 이름/공유/보관함 검색결과 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/search/all")
    fun postResultMatch(
        @Field("user_id") user_id: String,
        @Field("search_nm") search_nm: String,
        @Field("currPage") currPage: Int,
        @Field("pageSize") pageSize: Int
    ): Call<ResponseResultMatchDto>

    //TODO 050. 메인베너, 팝업베너, 이렇게활용 리스트
    @FormUrlEncoded
    @POST("/ntv/atp/list/banner")
    fun postMainBanner(
        @Field("banner_se_code") banner_se_code: String,
        @Field("menu_code") menu_code: String
    ): Call<ResponseMainBannerDto>
}