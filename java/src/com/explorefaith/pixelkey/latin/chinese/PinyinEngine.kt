package com.explorefaith.pixelkey.latin.chinese

/**
 * Compact offline Pinyin-to-Hanzi conversion engine for the Chinese (Pinyin) layout.
 *
 * Design mirrors bengali.RidmikParser: given the raw typed Latin word, produce the best
 * Hanzi conversion plus ranked alternates. Data is embedded in code so no network or
 * asset loading is required.
 *
 * Coverage notes: the syllable table includes all standard toneless Mandarin syllables;
 * per-syllable characters are ranked by rough everyday frequency; the phrase table covers
 * very common words. Syllables missing from the character table degrade gracefully to a
 * deterministic fallback character so typing never dead-ends.
 */
class PinyinEngine {

    companion object {
        // Deterministic fallback characters for syllables missing from the char table.
        val FALLBACK_CHARS = "的一是了我不人在他有这上们来到时大地为子中你说生国年着就那和要她出也得里后自以会家可下而过天去能对小多然于心学么之都好看起发当没成只如事把还用第样道想作种开"

        // Complete set of standard toneless Mandarin pinyin syllables.
        val SYLLABLES: Set<String> = (
                "a ai an ang ao " +
                "ba bai ban bang bao bei ben beng bi bian biao bie bin bing bo bu " +
                "ca cai can cang cao ce cen ceng cha chai chan chang chao che chen cheng chi chong chou chu chua chuai chuan chuang chui chun chuo ci cong cou cu cuan cui cun cuo " +
                "da dai dan dang dao de den deng di dia dian diao die ding diu dong dou du duan dui dun duo " +
                "e ei en eng er " +
                "fa fan fang fei fen feng fo fou fu " +
                "ga gai gan gang gao ge gei gen geng gong gou gu gua guai guan guang gui gun guo " +
                "ha hai han hang hao he hei hen heng hong hou hu hua huai huan huang hui hun huo " +
                "ji jia jian jiang jiao jie jin jing jiong jiu ju juan jue jun " +
                "ka kai kan kang kao ke kei ken keng kong kou ku kua kuai kuan kuang kui kun kuo " +
                "la lai lan lang lao le lei leng li lia lian liang liao lie lin ling liu lo long lou lu luan lun luo lv lue " +
                "ma mai man mang mao me mei men meng mi mian miao mie min ming miu mo mou mu " +
                "na nai nan nang nao ne nei nen neng ni nian niang niao nie nin ning niu nong nou nu nuan nuo nv nue " +
                "o ou " +
                "pa pai pan pang pao pei pen peng pi pian piao pie pin ping po pou pu " +
                "qi qia qian qiang qiao qie qin qing qiong qiu qu quan que qun " +
                "ran rang rao re ren reng ri rong rou ru rua rui run ruo " +
                "sa sai san sang sao se sen seng sha shai shan shang shao she shei shen sheng shi shou shu shua shuai shuan shuang shui shun shuo si song sou su suan sui sun suo " +
                "ta tai tan tang tao te teng ti tian tiao tie ting tong tou tu tuan tui tun tuo " +
                "wa wai wan wang wei wen weng wo wu " +
                "xi xia xian xiang xiao xie xin xing xiong xiu xu xuan xue xun " +
                "ya yan yang yao ye yi yin ying yo yong you yu yuan yue yun " +
                "za zai zan zang zao ze zei zen zeng zha zhai zhan zhang zhao zhe zhen zheng zhi zhong zhou zhu zhua zhuai zhuan zhuang zhui zhun zhuo zi zong zou zu zuan zui zun zuo"
                ).trim().split(" ").toSet()

        // Frequently-used multi-syllable words. Keys are concatenated syllables.
        val PHRASES: Map<String, String> = mapOf(
            "ninhao" to "你好", "xiexie" to "谢谢", "duibuqi" to "对不起", "meiguanxi" to "没关系",
            "zhongguo" to "中国", "beijing" to "北京", "shanghai" to "上海", "shenzhen" to "深圳",
            "guangzhou" to "广州", "xianggang" to "香港", "taiwan" to "台湾", "renmin" to "人民",
            "gongzuo" to "工作", "xuexi" to "学习", "xuesheng" to "学生", "laoshi" to "老师",
            "tongxue" to "同学", "pengyou" to "朋友", "jiaren" to "家人", "haizi" to "孩子",
            "mama" to "妈妈", "baba" to "爸爸", "gege" to "哥哥", "jiejie" to "姐姐",
            "didi" to "弟弟", "meimei" to "妹妹", "nanhai" to "男孩", "nvhai" to "女孩",
            "jintian" to "今天", "mingtian" to "明天", "zuotian" to "昨天", "xianzai" to "现在",
            "shijian" to "时间", "shihou" to "时候", "dianhua" to "电话", "shouji" to "手机",
            "diannao" to "电脑", "wangluo" to "网络", "youjian" to "邮件", "xinxi" to "信息",
            "xiaoxi" to "消息", "weixin" to "微信", "zhaopian" to "照片", "shipin" to "视频",
            "yinyue" to "音乐", "dianying" to "电影", "youxi" to "游戏", "yundong" to "运动",
            "lanqiu" to "篮球", "zuqiu" to "足球", "paobu" to "跑步", "chifan" to "吃饭",
            "mifan" to "米饭", "shuiguo" to "水果", "pingguo" to "苹果", "xiangjiao" to "香蕉",
            "kafei" to "咖啡", "niunai" to "牛奶", "xuexiao" to "学校", "gongsi" to "公司",
            "yiyuan" to "医院", "yinhang" to "银行", "shangdian" to "商店", "chaoshi" to "超市",
            "canting" to "餐厅", "jiudian" to "酒店", "gongyuan" to "公园", "tushuguan" to "图书馆",
            "jiaoshi" to "教室", "bangongshi" to "办公室", "huoche" to "火车", "feiji" to "飞机",
            "ditie" to "地铁", "qiche" to "汽车", "zixingche" to "自行车", "daolu" to "道路",
            "kaishi" to "开始", "jieshu" to "结束", "wenti" to "问题", "daan" to "答案",
            "banfa" to "办法", "fangshi" to "方式", "fangfa" to "方法", "yisi" to "意思",
            "xiwang" to "希望", "bangzhu" to "帮助", "xihuan" to "喜欢", "kuaile" to "快乐",
            "xingfu" to "幸福", "jiankang" to "健康", "anquan" to "安全", "zhuyi" to "注意",
            "lijie" to "理解", "jueding" to "决定", "jihua" to "计划", "mubiao" to "目标",
            "chenggong" to "成功", "shibai" to "失败", "nuli" to "努力", "renzhen" to "认真",
            "jianchi" to "坚持", "mingbai" to "明白", "zhidao" to "知道", "renwei" to "认为",
            "juede" to "觉得", "guanxi" to "关系", "shenghuo" to "生活", "shijie" to "世界",
            "guojia" to "国家", "chengshi" to "城市", "jiating" to "家庭", "huanjing" to "环境",
            "jingji" to "经济", "wenhua" to "文化", "lishi" to "历史", "shehui" to "社会",
            "kexue" to "科学", "jishu" to "技术", "tianqi" to "天气", "yingyu" to "英语",
            "hanyu" to "汉语", "putonghua" to "普通话", "ziji" to "自己", "dajia" to "大家",
            "women" to "我们", "nimen" to "你们", "tamen" to "他们", "zheli" to "这里",
            "nali" to "哪里", "shenme" to "什么", "zenme" to "怎么", "yinwei" to "因为",
            "suoyi" to "所以", "danshi" to "但是", "haishi" to "还是", "huozhe" to "或者",
            "ruguo" to "如果", "yijing" to "已经", "yinggai" to "应该", "bixu" to "必须",
            "keneng" to "可能", "yiqi" to "一起", "feichang" to "非常", "zuihou" to "最后",
            "shouxian" to "首先", "qishi" to "其实", "qingwen" to "请问", "huanying" to "欢迎",
            "zaijian" to "再见", "wanshang" to "晚上", "zaoshang" to "早上", "xiawu" to "下午",
            "shengri" to "生日", "chunjie" to "春节", "hongbao" to "红包", "liwu" to "礼物",
            "fukuan" to "付款", "shoukuan" to "收款", "weizhi" to "位置", "dizhi" to "地址",
            "mima" to "密码", "zhanghu" to "账户", "yonghu" to "用户", "denglu" to "登录",
            "sousuo" to "搜索", "xiazai" to "下载", "shangchuan" to "上传", "baocun" to "保存",
            "shanchu" to "删除", "fuzhi" to "复制", "zhantie" to "粘贴", "guanbi" to "关闭",
            "dakai" to "打开", "shezhi" to "设置", "jianpan" to "键盘", "shurufa" to "输入法",
            "pinyin" to "拼音", "hanzi" to "汉字", "meishu" to "美术", "laopo" to "老婆",
            "laogong" to "老公", "qinai" to "亲爱", "aigu" to "爱国"
        )

        // Per-syllable ranked characters (rough everyday frequency order). All keys are valid syllables.
        val SYLLABLE_CHARS: Map<String, String> = mapOf(
            "de" to "的地得", "shi" to "是时事使施十师石式识实食史世市示湿诗释拾逝狮柿拭", "yi" to "一以已意义易衣依亿宜移艺异议遗仪疑医椅役译亦翼乙益逸", "bu" to "不部步布补笔",
            "le" to "了乐勒", "zai" to "在再载灾栽宰", "ren" to "人认任忍仁韧刃纫", "wo" to "我握卧沃涡",
            "ta" to "他她它塔踏塌獭", "ni" to "你泥逆匿拟腻倪溺", "men" to "们门闷", "you" to "有又由右游友优油邮尤幼诱悠幽忧犹愉迂郁屿",
            "zhe" to "这者着折哲浙辙蔗遮褶", "zhong" to "中种重众钟忠终肿仲盅衷", "shang" to "上尚伤商赏晌裳", "xia" to "下夏峡狭侠暇吓虾辖霞",
            "he" to "和何河合核贺盒荷赫鹤黑嘿", "jiu" to "就九旧久救酒舅揪究灸玖", "yao" to "要药摇邀腰遥耀钥姚咬妖舀侥", "hao" to "好号毫豪浩耗郝壕嚎",
            "da" to "大打达答搭沓瘩", "xiao" to "小笑校消销晓肖孝效霄萧硝逍箫", "me" to "么", "ma" to "吗妈马码麻骂玛蚂", "ba" to "吧把八罢爸坝霸芭扒笆疤",
            "guo" to "国过果郭锅裹虢", "dui" to "对队堆兑敦镦", "jia" to "家加价假架佳甲嘉稼夹颊茄伽迦枷", "ke" to "可科克课客刻棵柯壳咳磕颗苛",
            "dao" to "到道岛倒刀导稻盗蹈悼捣祷叨忉", "shuo" to "说硕朔烁妁搠蒴", "nian" to "年念捻廿碾撵酿黏", "er" to "而二尔耳儿洱迩饵珥",
            "cheng" to "成城程称乘诚承盛呈惩澄橙骋丞酲", "li" to "里力立理利例礼离李丽历隶栗璃鲤俪荔黎笠痢俐浰", "wei" to "为位委维伟围微威违伪魏慰卫谓桅惟潍圩",
            "hui" to "会回汇悔惠慧挥辉毁恢绘贿卉徽诲彗蕙", "deng" to "等灯登邓凳瞪磴蹬噔", "xu" to "须需许序续绪叙絮蓄旭墟栩絮徐", "zhi" to "知只质指至制值智志置支执直纸止芝汁吱肢脂织职殖植掷侄滞蜘稚趾痔窒峙",
            "hu" to "户护湖互呼忽虎沪壶瑚蝴糊弧狐唬浒怙", "jin" to "进今金近仅紧尽劲禁晋锦津瑾烬觐襟", "qing" to "请青清轻情氢倾庆卿擎晴磬苘圊", "yuan" to "元员原源远院愿园圆援缘冤渊袁猿辕苑媛",
            "guan" to "关管馆官观冠惯贯棺纶莞矜鳏", "wang" to "王往网望忘妄汪亡枉芒莽莽", "kai" to "开凯慨铠揩垲恺", "zhu" to "住主注著朱助猪竹筑珠株诸祝驻贮煮铸伫洙苎铥",
            "dou" to "都斗豆逗陡痘蔸", "du" to "度都读独渡堵赌肚杜镀毒顿牍渎犊妒", "bei" to "北被倍备背辈悲杯贝碑卑惫狈呗悖蓓孛鞴", "rang" to "让嚷壤攘瓤禳穰",
            "gei" to "给", "zi" to "字自资子紫姿兹滋仔滓淄籽吱姊秭笫", "mei" to "没每美煤梅妹眉媒魅莓枚昧寐媚湄嵋镅", "hen" to "很狠恨痕",
            "lao" to "老劳牢捞涝姥佬烙酪唠銠", "zuo" to "做作坐左座昨佐琢凿唑祚酢笮", "na" to "那哪纳拿娜呐镎衲", "ceng" to "层曾蹭噌",
            "suo" to "所锁索莎琐梭唆蓑羧唢娑", "ne" to "呢讷", "duo" to "多朵躲舵垛哆惰踱堕跺刴沲", "xian" to "现先线显限鲜献县闲陷险嫌纤腺咸贤弦衔癣涎掀锨籼氙跹",
            "sheng" to "生声省剩胜圣升绳牲笙甥盛嵴晟眚", "di" to "的地底第低弟递帝敌迪堤滴缔蒂笛狄涤翟抵谛砥砥邸诋嘀羝氐", "yong" to "用永拥勇涌踊庸佣咏泳俑蛹痈臃慵雍邕饔", "zhen" to "真镇针阵珍震圳侦贞枕疹斟祯甄砧箴榛赈朕祯",
            "xiang" to "相向想象项香箱乡响享祥翔详镶巷湘蕈厢襄镶飨饷鲞", "zhao" to "找着照招罩赵兆肇昭棹爪诏", "qu" to "去区取曲趣驱屈渠娶瞿龋躯蕖祛氍觑衢阒", "tong" to "同通统痛童铜桶筒彤桐侗恸峒佟仝潼",
            "cong" to "从丛聪葱匆囱淙骢琮", "jian" to "见件间建简坚减检键荐渐尖肩艰监兼践贱鉴舰箭俭笺碱柬缄茧拣捡荐僭腱溅饯锏鲣鞯", "bi" to "比必避笔毕闭壁臂鼻彼逼碧璧蔽痹卑毙敝庇弊毖秕俾筚篦舭愎滗",
            "zhang" to "张章长涨掌帐障胀樟彰璋蟑丈仗杖账璋獐鄣幛瘴蟑", "peng" to "朋鹏彭碰棚蓬膨捧砰烹篷嘭硼砰堋", "ying" to "应影英营迎赢盈鹰荧莹颖蝇樱婴瑛膺萦茔滢璎颍", "jie" to "界接结解街节姐截杰洁捷劫戒届竭皆介藉诫匣诘孑拮碣羯婕讦",
            "qi" to "其期起七器气齐汽旗奇妻欺启泣砌崎骑棋乞迄戚栖戚砌崎骐绮琪祁祺圻芪淇萁骐", "hou" to "后候厚猴吼喉逅瘊堠後", "gong" to "工公共功供攻贡宫恭躬龚蚣觥珙", "wu" to "无五物武误午吴务舞屋污乌雾梧侮吾唔呜芜巫毋捂鹉蜈妩杌鹜",
            "gu" to "顾古鼓故固姑谷骨雇辜菇咕孤箍贾牯罟蛊钴嘏觚", "wen" to "问文闻温稳纹蚊吻紊瘟刎阌璺雯", "dang" to "当党档挡裆铛凼谠", "qian" to "前千钱浅欠迁签谦乾潜黔钳遣谴堑悭阡钎芊荨缱犍",
            "zheng" to "政正证争整征挣蒸睁筝郑狰怔铮峥峥诤钲", "shan" to "山善闪衫珊扇陕汕赡擅栅膳疝汕掸煽姗跚擅鄯钐", "ling" to "领零灵龄令玲岭凌伶羚铃翎聆珑囹泠苓绫棱呤", "zhan" to "站展占战沾粘瞻湛斩盏崭栈蘸毡詹旃谵",
            "shen" to "深神身申甚审伸沈婶肾渗绅砷呻什诜娠莘蜃矧", "ge" to "个各革格歌隔割阁戈葛胳疙铬骼舸仡硌嗝鬲", "chu" to "出处除初楚触储矗雏橱厨躇怵搐畜绌杵黜樗蜍", "xin" to "新心信芯薪锌欣辛馨忻寻囟莘歆",
            "bao" to "报保包爆堡饱暴胞宝薄苞褒雹鲍煲剥褴孢雹鸨趵龌", "shou" to "手受首收守寿授瘦售兽狩艏绶", "liang" to "两量良亮凉辆粮粱晾谅踉莨椋魉", "xie" to "些写谢协血鞋斜邪挟携懈谐泄泻屑械胁燮蟹偕卸屑邂逅亵渫獬绁榍薤",
            "dai" to "带代待袋戴贷呆歹逮傣怠殆岱贷埭甙迨", "que" to "却确缺雀鹊阙瘸炔阕悫", "zu" to "足组族祖阻租诅俚卒葅", "jiao" to "交教叫脚角觉较骄蕉浇椒胶绞郊窖骄跤姣椒茭皎嚼剿侥徼鲛峤",
            "jing" to "经京精惊境镜静竞敬警竟井景晶睛颈茎荆兢靖泾迳婧獲腈菁睛旌阱刭", "tui" to "推退腿褪颓蜕煺", "yue" to "月越约乐跃阅岳粤悦钺龠瀹栎", "pan" to "判盘盼攀潘畔叛磐拚蟠蹒泮祥",
            "rou" to "肉柔揉糅鞣", "chou" to "抽丑仇愁筹绸稠臭酬畴踌俦瘳雠丑", "mi" to "米密秘迷眯蜜谜靡弥觅泌眯宓谧咪猕縻汨弭冪", "guang" to "广光逛犷",
            "zou" to "走邹奏揍驺鲰陬", "lian" to "连联练莲炼脸廉帘怜镰链涟敛琏殓裢濂臁奁蔹", "gao" to "高告搞稿糕膏羔篙皋镐藁杲郜", "fan" to "反饭犯翻范繁凡返贩番蕃樊帆梵藩泛",
            "shu" to "书数术树属署束述输舒殊疏竖蜀鼠薯暑赎黍孰澍漱墅庶戍恕纾杸", "sui" to "随岁虽碎隧穗隋燧邃绥睢祟眭谇", "hua" to "话花化华划画哗桦猾滑骅铧", "zhui" to "追坠锥缀赘椎骓惴缒隹",
            "gun" to "滚棍辊衮磙绲鲧", "zhou" to "州周洲舟粥皱轴骤宙肘帚咒昼绉荮妯碲", "nin" to "您", "wa" to "娃挖瓦袜哇洼蛙佤娲呱",
            "cuo" to "错措搓挫磋蹉锉厝莝嵯瘥鹾", "zun" to "尊遵樽鳟撙", "qie" to "切且怯窃伽惬妾砌锲伽", "cui" to "脆催崔翠摧萃璀啐悴淬隹",
            "lue" to "略掠锊", "nue" to "虐疟", "lv" to "率绿律虑旅履侣屡缕驴吕铝滤氯褛偻稆膂虡", "jue" to "觉决绝掘爵嚼诀崛獗蕨橛蹶攫谲矍噱桷蕝玨",
            "miao" to "秒苗描庙妙瞄谬缪缈杪淼藐鹋", "niang" to "娘酿", "diu" to "丢铥", "weng" to "翁嗡瓮蕹",
            "seng" to "僧", "den" to "扽", "eng" to "鞥", "fou" to "否缶",
            "lo" to "咯", "yo" to "哟", "o" to "哦喔噢", "ei" to "诶欸",
            "a" to "啊阿", "e" to "饿俄额鹅恶遏扼噩鄂愕鳄厄轭垩苊萼", "n" to "嗯", "hm" to "噷",
            "hng" to "哼", "ang" to "昂肮盎", "ai" to "爱哎唉埃哀艾矮碍蔼隘暧嗑", "an" to "安按暗岸案俺氨鞍黯谙庵",
            "ao" to "奥傲熬澳袄懊拗坳嗷鏖鳌骜獒", "yu" to "于与语余预遇域育玉鱼雨狱誉欲宇羽豫愈驭喻逾渝隅娱渔芋峪尉郁御煜钰昱萸虞屿禹郁燠迂", "fa" to "发法罚乏伐罚筏阀砝", "fo" to "佛",
            "fou" to "否缶", "fei" to "费非飞肥肺废匪菲沸绯妃翡霏吠扉痱", "fen" to "分份粉纷奋愤粪氛焚坟汾酚芬吩鼢棼玢", "feng" to "风封丰疯逢缝蜂锋奉枫凤讽烽奉俸酆砜枫",
            "fu" to "夫服复福副符幅腐父腹付赋傅附富妇伏拂缚甫釜辅腑阜赴扶抚辐弗孚俘袱茯俯斧脯孵芙袱莆呋麸拊绂黻蚨罘韨", "pu" to "普铺浦扑朴溥莆璞圃菩堡埔瀑匍噗仆菩濮镤蹼", "kan" to "看刊勘堪坎侃砍槛龛", "gan" to "感干敢甘肝赶柑竿赣秆橄擀酎绀泔矸",
            "haha" to "哈哈"
        ).filterKeys { it in SYLLABLES }
    }

    /** Returns the best Hanzi conversion of the raw typed word, or the raw word itself when nothing matches. */
    fun toHanzi(raw: String): String {
        val all = getCandidates(raw)
        return all.firstOrNull() ?: raw
    }

    /** Returns ranked candidates: exact phrase first, then per-syllable conversion, then alternates. */
    fun getCandidates(raw: String): List<String> {
        if (raw.isNullOrEmpty()) return emptyList()
        val out = LinkedHashSet<String>()
        val s = raw.lowercase().filter { it in 'a'..'z' }
        if (s.isEmpty()) return emptyList()

        val (syllables, tail) = segment(s)

        // 1. Whole-syllable-sequence phrase match
        if (tail.isEmpty() && syllables.isNotEmpty()) {
            PHRASES[syllables.joinToString("")]?.let { out.add(it) }
        }

        // 2. Per-syllable conversion (primary chars)
        if (syllables.isNotEmpty()) {
            var chars = ""
            for (syll in syllables) {
                val c = SYLLABLE_CHARS[syll]?.firstOrNull()
                    ?: FALLBACK_CHARS[(syll[0].code + syll.length) % FALLBACK_CHARS.length]
                chars += c
            }
            out.add(chars + tail)

            // 3. Alternates: vary the last syllable's character
            val last = syllables.last()
            val alts = SYLLABLE_CHARS[last] ?: ""
            val head = if (chars.isEmpty()) "" else chars.substring(0, chars.length - 1)
            for (c in alts.drop(1)) {
                out.add(head + c + tail)
            }
        } else if (tail == s) {
            return emptyList() // nothing convertible
        }

        return out.toList()
    }

    private fun segment(raw: String): Pair<List<String>, String> {
        val s = raw.lowercase().filter { it in 'a'..'z' }
        if (s.isEmpty()) return Pair(emptyList(), "")
        val result = ArrayList<String>()
        var i = 0
        while (i < s.length) {
            var matched = ""
            for (len in minOf(6, s.length - i) downTo 1) {
                val cand = s.substring(i, i + len)
                if (cand in SYLLABLES) { matched = cand; break }
            }
            if (matched.isEmpty()) return Pair(result, s.substring(i))
            result.add(matched)
            i += matched.length
        }
        return Pair(result, "")
    }
}
