package com.justinmobile.core.exception;

/**
 * 异常代码，加入默认说明，这样减少配置文件的写入了
 * @author peak
 *
 */
public enum PlatformErrorCode {
	DEFAULT("-1", "默认，无具体信息"),
	
	UNCOMPLETED_METHOD("1", "未实现方法"),
	MOCK_BUSINESS_ERROR("2", "模拟业务平台异常"),
	
	SUCCESS("000000", "操作成功"),
	UNKNOWN_ERROR("000001", "未确认的系统错误"),
	DB_ERROR("000002", "数据库操作异常"),
	PARAM_ERROR("000003", "传入参数错误"),
	FILE_COPY_ERROR("000004", "复制文件错误"),
	CRYPTO_ERROR("000005", "加密传入参数错误"),
	WEBSERVICE_ERROR("000006", "业务平台通信失败"),
	FILE_FORMAT_ERROR("000007", "文件格式不正确"),
	FILE_NOT_EXIST("000008", "文件不存在"),
	
	UNAUTHORIZED_INTERFACE("000101", "{0}未授权"),
	
	HEX_STRING_ODD_LENGTH("100001", "十六进制字符串长度为奇数"),
	HEX_STRING_ERROR_CHAR("100002", "十六进制字符串有非法字符"),
	PAGE_PAGESIZE_ERROR("110001", "分页时必须传入每页显示的数量"),
	PAGE_PAGEORDER_ERROR("110002", "分页时传入的排序格式错误，格式为：fieldname_asc或者fieldname_desc"),
	PAGE_FILTER_PARAM_ERROR("110003", "分页时传入的查询参数格式错误，格式为：fieldname1=value&fieldname2=value"),
	
	SP_NOT_EXIST("200001", "SP不存在"), 
	SP_UNAVALIABLE("200002", "提供商状态不可用或在黑名单"),
	SP_RID_DISCARD("200003", "RID不匹配"),
	
	SD_NOT_EXIST("300001", "安全域不存在"),
	SD_EXISTED("300002", "安全域已存在"),
	UNKNOWN_KEY("300003", "未知密钥索引({0})"),
	SD_SPACE_NOT_FIXED("300004", "安全域不是签约空间安全域"),
	SD_AID_EXISTED("300004", "AID已存在"),
	
	USER_NOT_LOGIN("400001", "当前用户未登录"),
	USER_NAME_REDUPLICATE ("400002", "用户名已存在"),
	USER_MOBILE_REDUPLICATE ("400003", "手机号已被其他用户绑定"),
	USER_PASSWORD_ERROR ("400004", "用户密码输入错误"),
	USER_PASSWORD_TWICE_INCONFORMITY ("400005", "用户两次密码输入不一致"),
	USER_LOGIN_ERROR("400006", "登录失败，请检查用户名或者密码是否输入正确"),
	USER_EMAIL_REDUPLICATE ("400007", "电子邮箱已存在"),
	USER_NOT_EXIST ("400008", "用户不存在"),
	USER_NOT_DELETE_BY_ADMIN ("400009", "该功能只提供删除客服操作员、审核操作员以及管理员"),
	USER_STATUS_ERROR ("400010", "用户已被注销或平台拒绝该用户登录"),
	ROLE_NAME_REDUPLICATE ("410001", "角色名称已存在"),
	ROLE_IN_USE_BY_SOMEBODY ("410002", "角色已被用户使用"),
	ROLE_NOT_EXIST ("410003", "角色不存在，新的角色要先创建"),
	AUTH_NAME_REDUPLICATE ("420001", "权限名称已存在"),
	AUTH_NOT_EXIST ("420002", "权限不存在"),
	AUTH_IN_USE_BY_SOMEBODY ("420003", "权限已被用户使用"),
	AUTH_IN_USE_BY_ROLE ("420004", "权限已被角色使用"),
	MENU_NOT_EXIST ("430001", "菜单不存在"),
	MENU_IN_USE_BY_CHILD ("430002", "该菜单下有子菜单，请先删除子菜单再进行该菜单的删除操作"),
	MENU_NAME_REDUPLICATE_IN_SAME_LEVEL ("430003", "在同级别的菜单中，菜单名称不能重复"),
	MENU_IN_USE_BY_AUTH ("430004", "菜单已经被加入到权限管理，请先取消管理关系再进行删除操作"),
	MENU_HAS_CHILD_CHANGE_LEVEL ("430004", "菜单已存在子菜单，不能修改菜单的等级，请先取消所有子菜单的管理关系再进行操作"),
	SYSTEM_PARAMS_EXIST ("440001", "已存在相同的类型下的相同参数"),
	SYSTEM_PARAMS_TYPE_NOT_EXIST ("440002", "指定类型的参数不存在"),
	SYSTEM_PARAMS_TYPE_ERROR ("440003", "请选择参数类型"),
	RES_NAME_REDUPLICATE ("450001", "链接名称已存在"),
	RES_FILTER_REDUPLICATE ("450002", "链接地址已存在"),
	RES_IN_USE_BY_AUTH ("450003", "链接地址已经被加入到权限管理，请先取消管理关系再进行删除操作"),
	
	APPLICATION_AID_NOT_EXIST("500001", "应用AID不能为空"),
	APPLICATION_AID_SHORTER("500002", "应用AID长度小于5字节(10字符)"),
	APPLICATION_AID_LONGER("500003", "应用AID长度大于16字节(32字符)"),
	APPLICATION_AID_DISCARD("500004", "只能修改自己的应用"),
	APPLICAION_APPLET_AID_UNMACTH("500005", "必须有一个实例且只有一个实例的AID与应用的AID相同"), 
	APPLICAION_AID_REDULICATE("500006", "应用AID与{0}AID重复"),
	APPLICATION_NOT_INIT("500007", "应用状态不是初始化"), 
	APPLICATION_SP_DISCARD("500008", "只能操作自己的应用"),
	APPLICAION_URL_SERVICE_ERROR("500009", "业务平台URL或服务名错误"),
	APPLICATION_MOBILE_ICON_OVERSIZE("500010", "手机版图标的尺寸只能是50×50"),
	APPLICATION_COMMENT_REPEAT("500011", "您已对该应用进行了评论"),
	APPLICATION_LOAD_FILE_SD_DISCARD("500012", "应用所属安全域是主安全域，因此文件所属安全域必须是主安全域"),
	APPLICATION_NOT_AUDIT("500013", "应用状态不是已审核状态"), 
	
	APPLICATION_VERSION_NO_BLANK("510001", "应用版本号不能为空"),
	APPLICATION_VERSION_NOT_INIT("510002", "应用版本状态不是初始化"),
	APPLICATION_VERSION_NOT_PUBLISH("510003", "该版本不是已发布状态"),
	APPLICATION_VERSION_IS_PRESET("510004","该版本有预置关联关系，且该卡批次已经发卡"),
	
	LOAD_FILE_SP_DISCARD("520001", "只能操作自己的加载文件"),
	LOAD_FILE_AID_NOT_EXIST("520001", "加载文件AID不能为空"),
	LOAD_FILE_AID_SHORTER("520002", "加载文件AID长度小于5字节(10字符)"),
	LOAD_FILE_AID_LONGER("520003", "加载文件AID长度大于16字节(32字符)"),
	LOAD_FILE_CAP_UNEXIST("520005", "CAP文件不存在"),
	LOAD_FILE_AID_REDULICATE("520006", "加载文件AID与{0}AID重复"),
	LOAD_FILE_USED("520007", "加载文件的一个或多个版本已经被应用使用"), 
	LOAD_FILE_DEPENDED("520007", "加载文件的一个或多个版本已经被其他加载文件依赖"),
	LOAD_FILE_LOAD_PARAMS_ERROR("520008", "加载参数错误"),

	LOAD_FILE_VERSION_NO_BLANK("530001", "加载文件版本号不能为空"), 
	LOAD_FILE_VERSION_NO_REDUPLICATE("530002", "加载文件版本号重复"),
	LOAD_FILE_VERSION_PASER_ERROR("530003", "加载文件解析错误"),
	LOAD_FILE_NOT_CAP("530004", "加载文件的扩展名必须是.cap"),
	LOAD_FILE_VERSION_CIRCULAR_DEPENDENCE("530005", "存在循环依赖"),

	LOAD_MODULE_AID_REDUPLICATE("540001", "模块AID与{0}重复"),
	LOAD_MODULE_DEFINED_APPLET("540002", "模块已定义实例，请先删除实例"),
	LOAD_MODULE_AID_NOT_EXIST("540003", "模块AID不能为空"),
	LOAD_MODULE_AID_SHORTER("540004", "模块AID长度小于5字节(10字符)"),
	LOAD_MODULE_AID_LONGER("540005", "模块AID长度大于16字节(32字符)"),
	
	APPLET_AID_NOT_EXIST("550001", "实例AID不能为空"),
	APPLET_AID_SHORTER("550002", "实例AID长度小于5字节(10字符)"),
	APPLET_AID_LONGER("550003", "实例AID长度大于16字节(32字符)"),
	APPLET_AID_REDUPLICATE("550004", "实例AID与{0}重复"),
	APPLET_INSTALL_PARAMS_ERROR("550005", "安装参数错误"),
	
	APPLICAION_DEFINE_APPLET_SP_DISCARD("560001", "只能为自己的应用创建实例"),
	APPLICAION_SET_DOWNLOAD_ORDER_SP_DISCARD("560002", "只能为自己的应用设定下载顺序"), 
	APPLICAION_LOAD_FILE_UNEXIST("560003", "指定的次序信息不存在"),
	APPLICAION_SET_INSTALL_ORDER_SP_DISCARD("560004", "只能为自己的应用设定安装顺序"), 
	APPLICAION_REMOVE_IMPORT_SP_DISCARD("560005", "只能为自己的应用解除加载文件引用关系"), 
	APPLICAION_REMOVE_APPLET_SP_DISCARD("560006", "只能删除自己应用使用的实例"),
	APPLICAION_BUILD_IMPORT_SP_DISCARD("560007", "只能为自己的应用建立加载文件引用关系"),
	APPLICAION_AID_NOT_EXIST("560008", "应用AID不存在或者无对应AID的应用"), 
	APPLICAION_URL_NOT_EXIST("560009", "应用的业务平台URL无法连接"), 
	APPLICAION_SERVICE_NAME_NOT_EXIST("560010", "应用的业务平台服务名未找到"), 
	APPLICAION_ATLEAST_ONE_LOADFILE("560011", "应用至少要有一个加载文件"), 
	APPLICAION_ATLEAST_ONE_APPLET("560012", "应用至少要有一个实例"), 
	APPLICAION_TYPE_BY_USE("560013", "该应用类型已被应用使用，无法删除"), 
	APPLICAION_TYPE_BY_CHILD_USE("560014", "该应用类型已存在子类型，无法降为二级类型"), 
	APPLICAION_TYPE_REDUPLICATE_AT_SAME_LEVEL("560015", "同级别的应用类型不能存在相同的类型名称"), 
	APPLICAION_TYPE_IS_SHOW_INDEX("560016", "该应用类型已设置为首页展示"), 
	
	APPLICAION_CLIENT_EXSIT("570001","已经存在同一系统类型、同一系统版本、同一版本号、同一文件类型的客户端"),
	APPLICAION_CLIENT_NOT_EXSIT("570002","未找到该应用的客户端"),
	
	CARD_APPLICATION_NOT_DOWNLOADED("580001","终端上未下载该应用"),
	CARD_APPLICATION_ERROR_STATUS("580002","终端上应用状态异常"),
	CARD_APPLICATION_EMIRATED("580003","应用已经被迁出"),
	
	REQUISTION_NOT_EXIST("600001", "申请不存在"),
	MOBILE_SECTION_REPEAT("600002", "万号段重复，无法保存数据"), 
	
	CARD_IS_DISABLE("900001","卡不可用，请到移动营业厅恢复卡片"),
	TYPE_IS_NOT_EXIST("900002","此手机型号未找到"),
	CARD_IS_NOT_SUPPOT("900003","此终端芯片不被支持"),
	OLD_CARD_IS_NOT_EXIST("900004","被替换的终端不存在"),
	OLD_CARD_DIFFERENT("900005","上次恢复的应用尚未恢复完成,不能恢复新的终端应用"),
	CUSTOMER_CARD_NOT_EXIST("910006", "用户未绑定该终端或该终端已被加入黑名单"),
	SMS_ERROR("910007", "短信发送失败"),
	CARD_IS_EXIST("900008","终端已经绑定"),
	APP_IS_ARCHIVED("900009","此应用版本已经申请归档,不能重复申请"),
	APP_IS_PRESET("900010","该应用已被某批次预置，且该批次已发卡"),
	CARD_BASE_IS_EXIST("900011","已有相同编号批次,无法继续添加"),
	CARD_BASE_IS_EXIST_MODIFY("900012","已有相同编号批次,无法修改"),
	USER_MOBILE_BINED ("900013", "手机号码已经绑定"),
	MUST_lINK_FOR_DEFINE("900014", "未关联任何批次，无法完成定义"),
	CANT_OPT_BY_CARD("900015", "已存在该批次的卡片"),
	CANT_CHANGE_BY_APP_ARCHIVE("900016", "该应用已经归档,无法预置"),
	CANT_CHANGE_BY_SD_ARCHIVE("900017", "该安全域已经归档,无法预置"),
	CANT_ISD_NOT_PRESNAL("900018", "主安全域只能个人化模式"),
	CANT_OPT_BY_CARDNAME("900019", "批次\"{0}\"已存在卡片,{1}无法进行预置操作"),
	DO_IS_EXIST("900020", "此任务已存在"),
	OPT_IS_NOT_FINISH("900021", "更换终端还有应用未恢复,请在恢复应用功能中继续恢复"),
	CCI_IS_NOT_EXIST("900022", "当前终端不存在"),
	CCI_NAME_EXIST("900023", "同一用户下终端名称不能重复"),
	NOT_DOWN_APP("900024", "未下载应用"),
	NOT_DOWN_CLINET("900025", "没有对应的客户端版本"),
	CARDNO_INT_SCOPE("900026","当前卡号范围与已有批次存在重叠"),
	START_NO_GT_END_NO("900027","起始SEID只能小于或等于终止SEID"),
	TEST_FILE_IS_NOT_EXIST("900028","上传的测试报告文件已丢失"),
	FILE_DEL_FAIL("900029","测试报告文件删除失败"),
	NO_CONTACT_TEST_FILE("900030","至少需要上传一个测试报告文件"),
	NO_OPEN_NO_DOWN("900031","该手机所在地未开通本项业务，无法继续操作"),
	CARD_IS_PUBLISH("900032","该批次已发卡,无法修改"),
	APP_IS_REQ_PUBLISHED("900033","此应用版本已经申请发布,不能重复申请"),
	CUSTOMER_CARD_NOT_BIND("910034", "用户未绑定该终端"),
	SPACE_IS_ZERO_FILE("910035", "批次空间不足与文件\"{0}\"关联"),
	CARDBASE_SPACE_IS_ZERO("910036", "批次\"{0}\"空间不足,无法发布应用"),
	SPACE_IS_ZERO_APP("910037", "批次空间不足与应用\"{0}\"关联"),
	OPERATION_NOT_BELONG_THIS_TERMINAL("900038","当前终端不对应此任务"),
	CARD_NO_UNEXIST("900039","卡号不存在"),
	SD_NOT_DEL_BY_RULE("900040","该安全域删除规则为不能删除"),
	SD_IS_PRSET_FOR_NOT_DEL("900041","预置安全域不能从卡中删除"),
	NOT_FOUND_PARENT_SD("900042","文件或应用所属安全域尚未预置,请联系管理员"),
	CARD_SPACE_ERROR("900043","卡片空间不足以预置所需内容,操作失败"),
	GET_CARD_SPACE_ERROR("900044","获取空间大小发生错误"),
	CANT_ISD_NOT_UNPSET("900045", "不能改变主安全域预置信息"),
	CANT_ISD_MODIFY_FOR_CARDBASE("900046", "预置主安全域只能由卡批次修改"),
	PRE_CBSD_IS_EXIST("900047", "要替换的关联已经存在"),
	CARD_BASE_IS_REPORT("900048", "选择的卡批次已有测试结果"),
	NO_CONTACT_TEST_REPORT("900049","至少需要提交一个测试结果"),
	CARD_BASE_JUST_ONE_APPVER_FOR_APPLICATION("900050", "该批次已预置了应用\"{0}\"的其他版本"),
	CARD_BASE_JUST_ONE_LOADFILEVERSION_FOR_LOADFILE("900051","该批次已预置了文件\"{0}\"的其他版本"),
	CARD_BASE_SD_NOT_EXIST("900052", "{0}所属安全域尚未预置或预置模式不正确"),
	DEPENDENT_SD("900053", "已预置依赖该安全域的应用或文件，无法操作"),
	CARD_BASE_JUST_ONE_APPVER_FOR_PUBLISH("900054", "批次\"{0}\"已预置了该应用的其他版本"),
	CARD_BASE_SD_NOT_EXIST_FOR_PUBLISH_APP("900055", "该应用版本所属安全域尚未在批次\"{0}\"预置或预置模式不正确"),
	CARD_BASE_JUST_ONE_LOADFILEVERSION_FOR_LOADFILE_FOR_PUBLISH("900056","批次\"{0}\"已预置了文件\"{1}\"的其他版本"),
	CARD_BASE_SD_NOT_EXIST_FOR_PUBLISH_FILE("900057", "文件\"{0}\"该版本所属安全域尚未在批次\"{1}\"预置"),
	
	PROCESSING_TIMEOUT("1000", "业务处理时间超时"),
	DATA_ACCESS_ERROR("1001", "数据库操作失败"),
	SMS_COMMUNICATION_ERROR("1002", "短消息网关通讯失败"),
	CARD_DIRVER__COMMUNICATION_ERROR("1003", "读卡器控件通讯失败"),
	BIP_DIRVER__COMMUNICATION_ERROR("1004", "BIP通讯失败"),
	ENCRYPT_MODULE_ERROR("1005", "加密机模块处理失败"),
	CMS2AC_SECURITY_ERROR("1006", "CMS2AC安全通道创建失败"),
	BUILD_APDU_ERROR("1007", "生成APDU失败 "),
	BUILD_SMS_ERROR("1008", "短消息组包失败"),
	BUILD_CARD_DRIVER_MSG_ERROR("1009", "读卡器控件接口组包失败"),
	USR_NOT_REGISTER("1010", "用户未注册 "),
	BIP_CHANNEL_BUSY("1011", "用户BIP通道忙"),
	
	CARD_SPACE_SCARCITY("1100", "用户卡片无可用空间"),
	SD_SPACE_SCARCITY("1101", "用户安全域无可用空间"),
	DUPLICATED_APP_ERROR("1102", "应用已下载至卡片"),
	PERSONALIZE_APP_LOCK("1200", "应用已被锁定"),
	UPDATE_SD_KEY_LOCK("1300", "安全域已被锁定"),
	APP_NEVER_DOWNLOADED("1100", "用户未下载过该应用 "),
	
	INVALID_SESSION_STATUS("2000", "事务会话状态不存在"),
	SESSION_STATUS_ERROR("2001", "事务会话状态错误"),
	LOAD_BATCH_EXE_ERROR("2002", "LOAD指令序列执行失败"),
	BIP_OPEN_COUNTER_ERROR("2003", "BIP开通校验失败"),
	INVALID_COMM_TYPE("2004", "承载方式非法"),
	INVALID_TRANS_TYPE("2005", "交易类型非法"),
	INVALID_OPERATE_PROCESSOR("2006", "未找到应用相关操作"),
	INVALID_APDU_COMMAND("2007", "不合法的卡片操作指令"),
	INVALID_APDU_RESP_COMMAND("2008", "不合法的卡片响应"),
	INVALID_SCP02_I("2011", "安全通道协议SCP02实现选项非法"),
	INVALID_CMD_TYPE("2012", "非法的APDU指令 "),
	INVALID_APDU_PARAM("2013", "不合法的APDU参数"),
	INVALID_APP_STATUS("2014", "应用状态不是已发布的情况"),
	INVALID_APP_PERSONAL("2075", "应用状态不是个人化的情况"),
	APP_LOADFILE_NOT_FOUND("2015", "未找到应用的下载文件"),
	INVALID_TYPE_APP_DOWNLOAD("2016", "卡片不支持的应用"),
	INVALID_SD_STATUS("2017", "应用状态不是已审批通过的情况"),
	CARD_NOT_FOUND("2018", "卡片信息未找到"),
	APP_NOT_DOWNLOAD("2019", "应用未下载"),
	INVALID_KID_PARAM("2021", "非法的Kid参数"),
	INVALID_KIC_PARAM("2022", "非法的Kic参数"),
	PRE_INSTALL_APP_ERROR("2024", "应用已预置"),
	NOT_SUPORT_BATCH_NO_CARD("2025", "不支持该批次的卡"),
	CARD_LOCKED("2027", "卡片已锁定 "),
	APDU_INSTALL_SD_ERROR("2028", "安装安全域异常"),
	APDU_INSTALL_APP_ERROR("2029", "安装应用异常"),
	APDU_DELETE_SD_ERROR("2030", "删除安全域异常"),
	APDU_DELETE_APP_ERROR("2031", "删除应用异常"),
	APDU_LOCK_APP_ERROR("2032", "锁定应用异常"),
	APDU_UNLOCK_APP_ERROR("2033", "解锁应用异常"),
	APDU_UPDATE_KEY_ERROR("2034", "安全域密钥更新异常"),
	APDU_PERSONALIZE_APP_ERROR("2035", "应用个人化异常 "),
	APDU_LOAD_APP_ERROR("2036", "应用加载异常"),
	APDU_INSTALL_LOAD_ERROR("2037", "应用安装加载异常 "),
	APDU_GET_DATA_ERROR("2038", "GET DATA命令异常"),
	INVALID_CARD_SD_STATUS("2039", "卡片安全域状态异常"),
	INVALID_CARD_APP_STATUS("2040", "卡片应用状态异常"),
	APP_NOT_INSTALLED("2041", "应用未安装"),
	PERONSLIZE_APDU_LENGTH_ERROR("2042", "个人化指令长度错误"),
	INVALID_SCP("2043", "安全通道协议非法"),
	APDU_INIT_UPDATE_ERROR("2044", "初始化安全通道失败 "),
	APDU_EXT_AUTH_ERROR("2045", "外部认证失败"),
	SELECT_APP_ERROR("2046", "选择应用失败"),
	APDU_PP_DOWNLOAD_ERROR("2047", "PP DOWNLOAD命令异常"),
	APDU_TERMINAL_RESPONSE_ERROR("2048", "TERMINAL RESPONSE命令异常"),
	INVALID_PROVIDER_SESSION_ID("2049", "非法的业务会话ID"),
	PERSONALIZE_BATCH_EXE_ERROR("2050", "个人化指令序列执行失败"),
	TRANS_HAS_NOT_COMPLETED("2051", "该手机有未完成的操作"),
	REMOTE_CENTRAL_OTA_NOT_CONNECT("2052", "中央OTA无法连接"),
	APDU_PERSO_SD_ERROR("2053", "设置安全域个人化状态异常"),
	CARD_LF_SD_NOT_EXIST("2054", "卡片上没有可执行加载文件({0})关联的安全域({1})"),
	CARD_APP_SD_NOT_EXIST("2055", "卡片上没有应用({0})关联的安全域({1})"),
	CARD_CUSTOMER_STATUS_ERROR("2056", "手机用户状态非法或已加入黑名单"),
	CARD_CUSTOMER_MOBILE_ERROR("2057", "手机号码非法"),
	CARD_APP_NOT_PERSO("2058", "用户未订购该业务"),
	LOAD_FILE_OTHER_CARD_APP_INSTALLED("2060", "卡片上安装了相同加载文件的其他应用"),
	CARD_SD_RELATING_OBJECT("2061", "安全域存在关联对象,不能删除"),
	APDU_UPDATEED_KEY("2062", "密钥已经更新"),
	APDU_NO_UPDATE_KEY("2063", "未找到待更新密钥"),
	APP_EXTEND_IS_USED("2064", "该扩展被其他应用或扩展所依赖使用"),
	APP_CAN_NOT_DELETE("2065", "该应用不能从卡中删除"),
	SD_CAN_NOT_DELETE("2066", "该安全域不能从卡中删除"),
	REMOTE_BOSS_NOT_CONNECT("2067", "远程BOSS系统无法连接"),
	INVILIDATE_TOKEN_DATA("2068", "无效签名数据"),
	SHA1_FAIL("2069", "Token签名验证失败"),
	CRYPTO_FAILURE("2070", "加密失败 "),
	APP_PERSONAL_UNKNOWN_TYPE("2071", "未知个人化类型 "),
	GET_APPLET_STATUS_ERROR("2072", "获取卡片安装实例失败"),
	GET_PACKAGE_STATUS_ERROR("2073", "获取卡片安装文件失败"),
	CARD_BASE_NOT_REF_SD("2074", "该批次的卡片不支持当前安全域的创建({0})"),
	APDU_WRITE_TOKEN_ERROR("2075", "写入TOKEN失败"),
	APDU_STORE_MOBILE_NO_ERROR("2076", "写入手机号失败"),
	
	TRANS_PRE_OPERATION_FAIL("2098","业务平台预处理失败"),
	TRANS_SERVICE_FAIL("2099", "业务平台异常"),
	
	TRANS_SD_STATUS_ILLEGAL("2101", "安全域状态非法"),
	TRANS_APP_STATUS_ILLEGAL("2102", "应用状态非法"),
	TRANS_APP_VER_STATUS_ILLEGAL("2103", "应用版本状态非法"),
	TRANS_CARD_DISABLE("2104", "卡不可用，请到移动营业厅恢复卡片"),
	TRANS_CARD_LOCKED("2105", "卡已被锁定"),
	TRANS_CARD_NOT_LOST("2106", "卡未挂失"),
	TRANS_APP_AID_NOT_FOUND("2107", "AID对应的应用不存在"),
	TRANS_SD_AID_NOT_FOUND("2108", "AID对应的安全域不存在"),
	
	TRANS_DOWNLOAD_APP_NOT_FOUND("2201", "应用不存在"),
	TRANS_DOWNLOAD_APP_NOT_SUPPORT("2202", "卡片不支持此应用"),
	TRANS_DOWNLOAD_APP_OTHER_VERSION_EXIST("2203", "卡片上已存在应用其他版本"),
	TRANS_DOWNLOAD_APP_OTHER_LOAD_FILE_VERSION_EXIST("2204", "卡片上已存在应用所需加载文件的其他版本"),
	TRANS_DOWNLOAD_APP_VERSION_EXIST("2205", "应用已安装"),
	TRANS_DOWNLOAD_APP_APPLICATION_VERSION_UNEXIST("2206", "应用无指定版本"),
	TRANS_DOWNLOAD_APP_CARD_UNSUPPORT_APPLICATION_VERSION("2206", "卡不支持指定的应用版本"),
	TRANS_DOWNLOAD_APP_LOAD_FILE_SD_LOCK("2207", "加载文件({0})所属安全域({1})被锁定"),
	TRANS_DOWNLOAD_APP_APPLICATION_SD_LOCK("2208", "应用({0})所属安全域({1})被锁定"),
	TRANS_DOWNLOAD_APP_MOBILE_NO_UNSUPPORT_APPLICATION_VERSION("2209", "本手机号不支持该应用版本"),
	
	TRANS_CREATE_SSD_SD_UNEXSIT("2301", "指定AID的安全域不存在"),
	TRANS_CREATE_SSD_SD_EXSIT_ON_CARD("2302", "卡片上已存在安全域"),
	
	TRANS_MIGRATE_UNEMIGRATE("3001", "应用未被迁出，不能执行该操作"),
	
	TRANS_UPDATE_APP_LASTEST_IDENTICAL_APPLICATION_VERSION("4001", "卡上应用已经是最新版本"),
	TRANS_UPDATE_APP_SPECIALED_IDENTICAL_APPLICATION_VERSION("4002", "指定的应用版本与当前卡上的应用版本相同"),
	TRANS_UPDATE_APP_APP_VERSIONS_HAS_NOT_DISTINGUISH("4003", "应用的当前版本与待升级版本没有区别"),
	
	TRANS_UPDATE_KEY_ERROR_MODEL("4103", "委托管理安全域无法更新密钥"),
	
	TRANS_DELETE_APP_UNSUBSCRIBE("5001","该应用需要退订后才能删除"),
	
	TRANS_PERSO_APP_NOT_NECESSARY("6001","该应用不需要个人化"),
	
	TRANS_REG_REFUSE("7001","终端不能注册"),
	
	TRANS_REPLACE_INVALID_MOBILE_NO("8001","未输入手机号或手机号非法"),
	TRANS_REPLACE_IDENTICAL_MOBILE_NO("8002","新手机号与当前绑定的手机号相同"),
	
	INVOKE_PLATFORMSERVICE_ERROR("2515","调用平台服务异常"),
	SD_STATUS_ERROR("2507", "安全域状态错误"),
	SD_NOT_FOUND("2508", "未找到AID对应的安全域"),
	CARD_SD_NOT_FOUND("2509", "卡上未安装的安全域"),
	CARD_SD_STATUS_ERROR("2510", "卡上安全域状态非法"),
	CARD_APP_NOT_FOUND("2511", "卡上未安装应用"),
	CARD_APP_ERROR_STATUS("2512", "卡上应用状态异常"),
	SECURITYDOMAIN_NOT_FOUND("2505","未找到对应的安全域"), 
	SP_INVALID_ERROR("2516","不合法的应用提供商"), 
	SP_NOT_FOUND("2502","未找到服务商号对应的服务商"),
	SP_STATUS_ERROR("2517","应用提供商状态异常"),	
	SP_BLACKLIST_ERROR("2518","{0}应用提供商已加入黑名单"),
	SP_ACCREDIT_ERROR("2519","应用提供商授权失败：{0}"), 
	ISD_CAN_NOT_DELETE("2520","主安全域不能删除"), 
	CUSTOMER_NOT_REG("2600","该账户未注册，请先注册"),
	CUSTOMER_ACTIVE_INVALID("2601","激活已经失效，请重新申请激活邮件或短信"),
	CUSTOMER_ACTIVE_SUCCESS("2602","账户激活成功"),
	CUSTOMER_ALREADY_LOGIN("2603","账户已登录"),
	CUSTOMER_ACTIVE_CODE_INVALID("2604","激活码输入错误"),
	CUSTOMER_FINDPASS_CODE_INVALID("2605","验证码输入错误"),
	CUSTOMER_FINDPASS_CODE_OVERTIME("2606","验证码过期"),

	CUSTOMER_ALREADY_ACTIVE("2604","已经激活"), 
	CUSTOMER_PASSWORD_RESET_INVALID("2605","密码重置已失效"),
	SD_INSTALLPARAMS_PARSE_ERROR("2700","安全域安装参数解析异常"),
	SD_INSTALLPARAMS_DELETERULE_ERROR("2701","安全域的删除规则必须与安装参数的配置保持一致"),
	SD_INSTALLPARAMS_TRANSFER_ERROR("2702","公共第三方安全域或DAP安全域，必须接受迁移"),
	SD_INSTALLPARAMS_DELETEAPP_ERROR("2703","公共第三方安全域或DAP安全域，必须接受从主安全域发起的应用删除"),
	APPLICATION_CLIENT_AREADY_RELEASE("2800","该手机钱包已经发布，不能删除"),
	APPLICATION_CLIENT_RELEASE_TWICE("2801","该手机钱包已经发布，不必重新发布"),
	APPLICATION_CLIENT_VERSION_REPEAT("2802","该手机钱包的版本已上传，请修正新版本"),
	APPLICATION_CLIENT_VERSION_NOT_FOUND("2803","未找到支持当前操作系统的手机钱包，请与客服联系"),
	FEE_RULE_FUNCTION_PER_EXIST("2901","该应用提供商的功能按次计费规则已经存在"),
	FEE_RULE_FUNCTION_MONTH_EXIST("2902","该应用提供商的功能包月计费规则已经存在"),
	FEE_RULE_SPACE_EXIST("2905","该应用或安全域的空间计费规则已经存在"),
	FEE_RULE_FUNCTION_EXIST("2906","该应用提供商的功能计费规则已经存在"),
	
	APPLICATION_SERVICE_ALREAY_EXIST("3000","该应用或安全域的服务接口已经存在"),
	
	MISMATCH_CHALLENGE_NO("4000", "随机数错误"),
	TOKEN_MISMATCH("4010", "token不匹配"), 
	
	SECURE_UNKNOWN_ALGORITHM("5001","未知算法")
	;

	private String code;

	private String defaultMessage;

	PlatformErrorCode(String code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	public String getErrorCode() {
		return this.code;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}

}
