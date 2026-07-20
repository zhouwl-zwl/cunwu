


CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    phone VARCHAR(20),
    real_name VARCHAR(50),
    avatar VARCHAR(200),
    role VARCHAR(20) DEFAULT 'VILLAGER',
    status INT DEFAULT 1,
    audit_status INT DEFAULT 0,
    address VARCHAR(200),
    group_name VARCHAR(50),
    village VARCHAR(100),
    is_party_member VARCHAR(10),
    party_type VARCHAR(20),
    join_party_date DATETIME,
    party_branch VARCHAR(100),
    party_score INT DEFAULT 0,
    duty VARCHAR(50),
    manage_business VARCHAR(200),
    start_work_date DATETIME,
    grid_area VARCHAR(100),
    id_card VARCHAR(50),
    emergency_name VARCHAR(50),
    emergency_phone VARCHAR(20),
    last_login_time DATETIME,
    create_time DATETIME,
    update_time DATETIME
);

CREATE TABLE IF NOT EXISTS village_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    village_name VARCHAR(100),
    population INT DEFAULT 0,
    area DOUBLE DEFAULT 0,
    household_count INT DEFAULT 0,
    group_count VARCHAR(50),
    location VARCHAR(200),
    history TEXT,
    honors TEXT,
    products TEXT,
    total_assets DOUBLE DEFAULT 0,
    carousel_images TEXT,
    create_time DATETIME,
    update_time DATETIME
);

CREATE TABLE IF NOT EXISTS org_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    avatar VARCHAR(200),
    position VARCHAR(50),
    division VARCHAR(100),
    phone VARCHAR(20),
    org_type VARCHAR(20),
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS public_notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    notice_type VARCHAR(20),
    files TEXT,
    start_time DATETIME,
    end_time DATETIME,
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS demand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    demand_type VARCHAR(20),
    content TEXT,
    images TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    handler_id BIGINT,
    handler_name VARCHAR(50),
    solution TEXT,
    result_images TEXT,
    user_id BIGINT,
    create_time DATETIME,
    update_time DATETIME
);

CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    images TEXT,
    type VARCHAR(20),
    is_top INT DEFAULT 0,
    status INT DEFAULT 1,
    user_id BIGINT,
    expire_time DATETIME,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS asset (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20),
    asset_value DOUBLE DEFAULT 0,
    location VARCHAR(200),
    status VARCHAR(20),
    buy_date DATETIME,
    depreciation_rate DOUBLE,
    description TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS fund_flow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    amount DOUBLE DEFAULT 0,
    description VARCHAR(500),
    category VARCHAR(50),
    flow_date DATETIME,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20),
    area DOUBLE DEFAULT 0,
    location VARCHAR(200),
    status VARCHAR(20),
    contract_no VARCHAR(50),
    contract_date DATETIME,
    expire_date DATETIME,
    lessee VARCHAR(100),
    rent DOUBLE,
    description TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS party_building (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    type VARCHAR(20),
    images TEXT,
    participants VARCHAR(500),
    activity_date DATETIME,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS rural_revitalization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    title VARCHAR(200),
    content TEXT,
    images TEXT,
    responsible_person VARCHAR(50),
    group_name VARCHAR(50),
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS environment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    title VARCHAR(200),
    content TEXT,
    before_images TEXT,
    after_images TEXT,
    responsible_person VARCHAR(50),
    group_name VARCHAR(50),
    status VARCHAR(20),
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS comprehensive_governance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20),
    title VARCHAR(200),
    content TEXT,
    parties VARCHAR(500),
    handler VARCHAR(50),
    result TEXT,
    status VARCHAR(20),
    group_name VARCHAR(50),
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS industry (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20),
    content TEXT,
    images TEXT,
    income DOUBLE DEFAULT 0,
    responsible_person VARCHAR(50),
    create_time DATETIME
);

INSERT INTO sys_user (username, password, phone, real_name, role, status, audit_status, village, is_party_member, party_type, join_party_date, party_branch, party_score, duty, manage_business, start_work_date, grid_area, id_card, address, emergency_name, emergency_phone, create_time) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13800138000', '管理员', 'ADMIN', 1, 1, '幸福村', '是', '正式党员', '2010-07-01', '第一党支部', 95, '村党支部书记', '党建,乡村振兴', '2018-03-10', '第一网格', '430101198005121234', '幸福村1组', '张四', '139001394567', NOW()),
('village_official', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13900139000', '村干部', 'VILLAGE_OFFICIAL', 1, 1, '幸福村', '是', '正式党员', '2015-07-01', '第一党支部', 86, '网格员', '综治调解,便民服务', '2020-05-15', '第三网格', '430101198508205678', '幸福村2组', '李五', '138001385678', NOW()),
('resident_official', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13700137000', '驻村干部', 'RESIDENT_OFFICIAL', 1, 1, '幸福村', '是', '正式党员', '2008-07-01', '派出单位党支部', 92, '驻村工作队队员', '乡村振兴,党建', '2023-01-01', '全村', '430101197812109012', '村委会宿舍', '王六', '136001361234', NOW()),
('villager', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '13600136000', '村民', 'VILLAGER', 1, 1, '幸福村', '否', NULL, NULL, NULL, 0, '普通村民', NULL, NULL, NULL, '430101199003153456', '幸福村3组', '赵七', '135001357890', NOW());

INSERT INTO village_info (village_name, population, area, household_count, group_count, location, history, honors, products, total_assets, create_time) VALUES
('幸福村', 2580, 12.5, 680, '12个村民组', '浙江省杭州市余杭区', '幸福村历史悠久，始建于明朝，距今已有600多年历史。村内保留了众多明清古建筑，是省级历史文化名村。', '省级文明村、省级生态村、市级美丽乡村示范村', '龙井茶、竹笋、杨梅、草莓', 2850000, NOW());

INSERT INTO org_member (name, avatar, position, division, phone, org_type, create_time) VALUES
('张三', '', '党支部书记', '负责全面工作', '13800138001', '支委', NOW()),
('李四', '', '党支部副书记', '负责党建、组织', '13800138002', '支委', NOW()),
('王五', '', '纪检委员', '负责纪检监察', '13800138003', '支委', NOW()),
('赵六', '', '村委会主任', '负责行政全面工作', '13800138004', '村委', NOW()),
('钱七', '', '村委会副主任', '负责财务、三资管理', '13800138005', '村委', NOW()),
('孙八', '', '妇女主任', '负责妇联、计生', '13800138006', '村委', NOW()),
('周九', '', '监委主任', '负责村务监督', '13800138007', '监委', NOW()),
('吴十', '', '监委委员', '负责财务监督', '13800138008', '监委', NOW());

INSERT INTO public_notice (title, content, notice_type, status, create_time) VALUES
('2024年第一季度党费公示', '全体党员：\n\n现将2024年第一季度党费收缴情况公示如下，请各位党员查阅。\n\n公示时间：2024年4月1日-4月7日\n如有疑问，请联系纪检委员王五。', '党务公开', 1, NOW()),
('2024年村庄规划公示', '根据上级要求，结合我村实际情况，制定了2024年村庄发展规划，现予以公示。\n\n公示时间：2024年3月15日-3月21日', '村务公开', 1, NOW()),
('2024年1月财务收支报表', '收入：125000元\n支出：89000元\n结余：36000元\n\n详细收支明细见附件。', '财务公开', 1, NOW());

INSERT INTO notification (title, content, type, is_top, status, create_time) VALUES
('关于开展春季爱国卫生运动的通知', '全体村民：\n\n为进一步改善村庄环境卫生，经村两委研究决定，定于3月20日开展春季爱国卫生运动，请各村民组积极组织参与。', '民生通知', 1, 1, NOW()),
('2024年城乡居民医保缴费通知', '2024年城乡居民医保缴费工作已开始，请尚未缴费的村民及时前往村委会办理。', '政策通知', 0, 1, NOW()),
('关于召开村民代表大会的通知', '定于3月25日下午2点在村委会会议室召开村民代表大会，请各位代表准时参加。', '村务通知', 0, 1, NOW());

INSERT INTO asset (name, type, asset_value, location, status, create_time) VALUES
('村委会办公楼', '房屋', 500000, '村中心', '使用中', NOW()),
('文化活动中心', '房屋', 300000, '村西头', '使用中', NOW()),
('农用拖拉机', '设备', 50000, '村委会大院', '使用中', NOW()),
('灌溉水泵', '设备', 20000, '农田区', '使用中', NOW());

INSERT INTO fund_flow (type, amount, description, category, flow_date, create_time) VALUES
('INCOME', 50000, '上级财政拨款', '财政收入', NOW(), NOW()),
('INCOME', 30000, '土地流转收入', '经营收入', NOW(), NOW()),
('EXPENSE', 20000, '环境卫生整治费用', '管理费用', NOW(), NOW()),
('EXPENSE', 15000, '文化活动经费', '其他支出', NOW(), NOW());

INSERT INTO resource (name, type, area, location, status, contract_no, lessee, rent, create_time) VALUES
('东山坡林地', '林地', 500, '村东', '已流转', 'HT2024001', '杭州林业公司', 50000, NOW()),
('南田耕地', '耕地', 200, '村南', '已流转', 'HT2024002', '农业合作社', 30000, NOW());

INSERT INTO industry (name, type, content, income, responsible_person, create_time) VALUES
('茶叶合作社', '合作社', '主营龙井茶种植、加工、销售', 150000, '张三', NOW()),
('生态果园', '特色产业', '种植杨梅、草莓等水果', 80000, '李四', NOW());

-- ============== 模块1: 党建工作 ==============
CREATE TABLE IF NOT EXISTS party_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    birth_date DATETIME,
    join_date DATETIME,
    phone VARCHAR(20),
    address VARCHAR(200),
    party_type VARCHAR(20),
    org_relation_status VARCHAR(20),
    score INT DEFAULT 0,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS party_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    activity_type VARCHAR(20),
    content TEXT,
    participants VARCHAR(500),
    images TEXT,
    activity_date DATETIME,
    recorder VARCHAR(50),
    create_time DATETIME
);

-- ============== 模块2: 政务工作 ==============
CREATE TABLE IF NOT EXISTS village_meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    meeting_type VARCHAR(20),
    content TEXT,
    participants VARCHAR(500),
    images TEXT,
    meeting_date DATETIME,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS document_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    doc_type VARCHAR(20),
    doc_no VARCHAR(50),
    source VARCHAR(100),
    content TEXT,
    files TEXT,
    deadline DATETIME,
    status VARCHAR(20),
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS archive_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50),
    content TEXT,
    files TEXT,
    create_time DATETIME
);

-- ============== 模块3: 便民服务 ==============
CREATE TABLE IF NOT EXISTS convenience_service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_type VARCHAR(20),
    applicant_name VARCHAR(50),
    applicant_phone VARCHAR(20),
    content TEXT,
    status VARCHAR(20),
    handler VARCHAR(50),
    result TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS special_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    group_type VARCHAR(20),
    phone VARCHAR(20),
    address VARCHAR(200),
    visit_record TEXT,
    last_visit_date DATETIME,
    create_time DATETIME
);

-- ============== 模块5: 安全防护 ==============
CREATE TABLE IF NOT EXISTS safety_hazard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(200),
    hazard_type VARCHAR(20),
    description TEXT,
    images TEXT,
    deadline DATETIME,
    status VARCHAR(20),
    handler VARCHAR(50),
    result TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS flood_control (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_type VARCHAR(20),
    name VARCHAR(100),
    content TEXT,
    location VARCHAR(200),
    images TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS fire_safety (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(200),
    check_content TEXT,
    images TEXT,
    issues TEXT,
    handler VARCHAR(50),
    check_date DATETIME,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS food_safety (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(200),
    check_content TEXT,
    images TEXT,
    issues TEXT,
    handler VARCHAR(50),
    check_date DATETIME,
    create_time DATETIME
);

-- ============== 模块6: 乡村振兴扩展 ==============
CREATE TABLE IF NOT EXISTS toilet_reform (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    household_name VARCHAR(50),
    address VARCHAR(200),
    reform_type VARCHAR(20),
    before_images TEXT,
    after_images TEXT,
    verify_status VARCHAR(20),
    subsidy_amount DOUBLE DEFAULT 0,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS sewage_treatment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(200),
    facility_name VARCHAR(100),
    content TEXT,
    status VARCHAR(20),
    images TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS garbage_collection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cleaner_name VARCHAR(50),
    area VARCHAR(200),
    frequency VARCHAR(50),
    check_date DATETIME,
    notes TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS forest_fire (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patrol_person VARCHAR(50),
    patrol_date DATETIME,
    route VARCHAR(200),
    content TEXT,
    images TEXT,
    issues TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS road_traffic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_type VARCHAR(20),
    location VARCHAR(200),
    content TEXT,
    images TEXT,
    handler VARCHAR(50),
    create_time DATETIME
);

-- ============== 模块7: 综合治理扩展 ==============
CREATE TABLE IF NOT EXISTS dispute (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dispute_type VARCHAR(20),
    parties VARCHAR(500),
    description TEXT,
    images TEXT,
    handler VARCHAR(50),
    mediation_record TEXT,
    result TEXT,
    status VARCHAR(20),
    create_time DATETIME
);

-- ============== 公共模块 ==============
CREATE TABLE IF NOT EXISTS task_supervision (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    source VARCHAR(20),
    content TEXT,
    deadline DATETIME,
    status VARCHAR(20),
    assignee VARCHAR(50),
    result TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS document_library (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(20),
    file_path VARCHAR(500),
    file_type VARCHAR(20),
    description TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator VARCHAR(50),
    operation_type VARCHAR(20),
    module VARCHAR(50),
    content TEXT,
    create_time DATETIME
);

-- ============== 测试数据: 党建工作 ==============
INSERT INTO party_member (name, gender, birth_date, join_date, phone, address, party_type, org_relation_status, score, create_time) VALUES
('张三', '男', '1980-05-12', '2005-07-01', '13800138001', '幸福村1组', '正式', '在册', 95, NOW()),
('李四', '女', '1985-08-20', '2010-07-01', '13800138002', '幸福村2组', '预备', '在册', 80, NOW()),
('王五', '男', '1990-03-15', '2022-07-01', '13800138003', '幸福村3组', '积极分子', '在册', 60, NOW());

INSERT INTO party_activity (title, activity_type, content, participants, images, activity_date, recorder, create_time) VALUES
('2024年第一季度支部党员大会', '三会一课', '传达学习上级党组织文件精神，研究部署2024年党建工作', '全体党员', '', '2024-03-15 14:00:00', '张三', NOW()),
('主题党日活动-学习党史', '主题党日', '组织全体党员赴红色教育基地参观学习，重温入党誓词', '全体党员', '', '2024-04-20 09:00:00', '李四', NOW()),
('2024年组织生活会', '组织生活会', '开展批评与自我批评，查找问题并制定整改措施', '支部委员', '', '2024-06-10 14:00:00', '张三', NOW());

-- ============== 测试数据: 政务工作 ==============
INSERT INTO village_meeting (title, meeting_type, content, participants, images, meeting_date, create_time) VALUES
('2024年第一次村两委会议', '村两委会议', '研究部署2024年村庄发展规划及重点项目推进工作', '村两委成员', '', '2024-01-10 09:00:00', NOW()),
('2024年第一季度村民代表大会', '村民代表大会', '审议2024年村庄建设项目及资金使用计划', '村民代表35人', '', '2024-03-25 14:00:00', NOW());

INSERT INTO document_record (title, doc_type, doc_no, source, content, files, deadline, status, create_time) VALUES
('关于做好春耕生产工作的通知', '上级来文', 'XZ2024001', '乡镇政府', '要求各村做好春耕备耕工作，确保粮食生产安全', '', '2024-04-30 00:00:00', '已办结', NOW()),
('2024年美丽乡村建设申报书', '本村上报', 'SC2024001', '幸福村', '申报2024年美丽乡村建设项目，涉及环境整治、基础设施提升', '', '2024-05-15 00:00:00', '待办', NOW()),
('关于开展安全生产大检查的通知', '上级来文', 'XZ2024002', '县应急管理局', '要求各村开展安全生产隐患排查整治工作', '', '2024-07-30 00:00:00', '待办', NOW());

INSERT INTO archive_record (title, category, content, files, create_time) VALUES
('2023年度村务工作总结', '年度总结', '幸福村2023年度村务工作总结报告', '', NOW()),
('2023年财务决算资料', '财务档案', '幸福村2023年度财务决算相关资料', '', NOW());

-- ============== 测试数据: 便民服务 ==============
INSERT INTO convenience_service (service_type, applicant_name, applicant_phone, content, status, handler, result, create_time) VALUES
('医保', '王大爷', '13800138101', '代办2024年城乡居民医保缴费', '已办结', '李四', '已完成缴费', NOW()),
('养老', '张奶奶', '13800138102', '协助办理城乡居民养老保险待遇领取', '办理中', '王五', '', NOW()),
('高龄补贴', '赵爷爷', '13800138103', '申请80岁以上高龄补贴', '待办', '', '', NOW());

INSERT INTO special_group (name, group_type, phone, address, visit_record, last_visit_date, create_time) VALUES
('小明', '留守儿童', '13800138201', '幸福村1组', '2024年3月走访：生活学习正常', '2024-03-10 00:00:00', NOW()),
('李奶奶', '独居老人', '13800138202', '幸福村2组', '2024年3月走访：身体状况良好', '2024-03-12 00:00:00', NOW()),
('王师傅', '残疾人', '13800138203', '幸福村3组', '2024年2月走访：建议申请残疾人补贴', '2024-02-20 00:00:00', NOW());

-- ============== 测试数据: 安全防护 ==============
INSERT INTO safety_hazard (location, hazard_type, description, images, deadline, status, handler, result, create_time) VALUES
('村东商铺', '商铺', '灭火器过期，电线老化', '', '2024-04-15 00:00:00', '已整改', '张三', '已更换灭火器，重做电线', NOW()),
('村南农家乐', '农家乐', '厨房燃气罐存放不规范', '', '2024-04-20 00:00:00', '整改中', '李四', '', NOW()),
('村西老旧房屋', '危房', '墙体裂缝，存在倒塌风险', '', '2024-05-10 00:00:00', '待整改', '', '', NOW());

INSERT INTO flood_control (item_type, name, content, location, images, create_time) VALUES
('防汛物资', '编织袋', '库存500条，状态完好', '村委会仓库', '', NOW()),
('地质灾害点', '村北滑坡隐患点', '雨季需重点监测，已设置警示标志', '村北山坡', '', NOW()),
('转移群众', '低洼地区群众', '已登记12户32人，制定转移方案', '村南低洼区', '', NOW());

INSERT INTO fire_safety (location, check_content, images, issues, handler, check_date, create_time) VALUES
('村委会办公楼', '检查消防设施、疏散通道', '', '灭火器需年检', '张三', '2024-03-05 00:00:00', NOW()),
('村文化活动中心', '检查电气线路、消防器材', '', '无问题', '李四', '2024-03-08 00:00:00', NOW());

INSERT INTO food_safety (location, check_content, images, issues, handler, check_date, create_time) VALUES
('村小学食堂', '检查食品采购、储存、加工环节', '', '留样不规范', '王五', '2024-03-12 00:00:00', NOW()),
('村南农家乐', '检查从业人员健康证、食品来源', '', '健康证齐全', '李四', '2024-03-15 00:00:00', NOW());

-- ============== 测试数据: 乡村振兴扩展 ==============
INSERT INTO toilet_reform (household_name, address, reform_type, before_images, after_images, verify_status, subsidy_amount, create_time) VALUES
('张大爷家', '幸福村1组', '水冲式', '', '', '已验收', 1500, NOW()),
('李大妈家', '幸福村2组', '三格式', '', '', '已验收', 1200, NOW()),
('王师傅家', '幸福村3组', '水冲式', '', '', '待验收', 0, NOW());

INSERT INTO sewage_treatment (location, facility_name, content, status, images, create_time) VALUES
('村东生活区', '一体化污水处理设施', '处理规模50吨/日，服务农户80户', '正常运行', '', NOW()),
('村西生活区', '人工湿地', '处理规模30吨/日，服务农户50户', '需维护', '', NOW());

INSERT INTO garbage_collection (cleaner_name, area, frequency, check_date, notes, create_time) VALUES
('赵保洁', '村东片区', '每日一次', '2024-03-20 00:00:00', '保洁情况良好', NOW()),
('钱保洁', '村西片区', '每日一次', '2024-03-20 00:00:00', '需加强垃圾分类宣传', NOW());

INSERT INTO forest_fire (patrol_person, patrol_date, route, content, images, issues, create_time) VALUES
('张三', '2024-03-19 00:00:00', '村东山林地', '巡查未见异常，提醒上山人员注意防火', '', '无', NOW()),
('李四', '2024-03-21 00:00:00', '村北山林地带', '发现违规野外用火一处，已制止', '', '已处置', NOW());

INSERT INTO road_traffic (record_type, location, content, images, handler, create_time) VALUES
('安全劝导', '村口主路', '劝导骑电动车村民佩戴头盔', '', '王五', NOW()),
('道路养护', '村南机耕路', '修补路面坑洼，清理排水沟', '', '赵六', NOW());

-- ============== 测试数据: 综合治理扩展 ==============
INSERT INTO dispute (dispute_type, parties, description, images, handler, mediation_record, result, status, create_time) VALUES
('邻里纠纷', '张三、李四', '两家因宅基地边界问题产生纠纷', '', '王调解员', '现场调解，依据土地证确认边界', '双方达成和解', '已调解', NOW()),
('土地纠纷', '王某、村集体', '流转土地租金支付时间争议', '', '李调解员', '组织双方协商', '', '调解中', NOW());

-- ============== 测试数据: 公共模块 ==============
INSERT INTO task_supervision (title, source, content, deadline, status, assignee, result, create_time) VALUES
('推进美丽乡村建设工作', '乡镇', '按时间节点完成美丽乡村建设各项任务', '2024-12-31 00:00:00', '办理中', '张三', '', NOW()),
('落实防汛备汛工作', '县级', '完成防汛物资储备、隐患排查、转移方案制定', '2024-06-30 00:00:00', '待办', '李四', '', NOW()),
('报送2024年半年度工作总结', '乡镇', '汇总上报2024年上半年各项工作开展情况', '2024-07-15 00:00:00', '已完成', '王五', '已报送', NOW());

INSERT INTO document_library (title, category, file_path, file_type, description, create_time) VALUES
('2024年中央一号文件', '政策通知', '/docs/2024_policy.docx', 'docx', '关于学习运用"千村示范、万村整治"工程经验有力有效推进乡村全面振兴的意见', NOW()),
('党支部工作条例', '党建', '/docs/party_regulation.pdf', 'pdf', '中国共产党支部工作条例（试行）', NOW()),
('农村集体经济组织财务制度', '三资', '/docs/finance_rule.pdf', 'pdf', '农村集体经济组织财务制度', NOW());

INSERT INTO operation_log (operator, operation_type, module, content, create_time) VALUES
('admin', '新增', '党建工作', '新增党员信息：张三', NOW()),
('admin', '修改', '便民服务', '更新代办事项状态：医保缴费', NOW()),
('village_official', '新增', '安全防护', '新增安全隐患排查记录', NOW());

CREATE TABLE IF NOT EXISTS user_info_audit (
    audit_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    village_id BIGINT,
    village_name VARCHAR(100),
    change_type VARCHAR(20),
    old_data TEXT,
    new_data TEXT,
    audit_status INT DEFAULT 0,
    audit_admin_id BIGINT,
    audit_admin_name VARCHAR(50),
    audit_opinion VARCHAR(500),
    submit_time DATETIME,
    audit_time DATETIME,
    create_time DATETIME DEFAULT NOW()
);

-- ============== 模块8: 信访矛盾调解 ==============
CREATE TABLE IF NOT EXISTS petition (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    petition_type VARCHAR(20),
    petitioner_name VARCHAR(50),
    petitioner_phone VARCHAR(20),
    parties VARCHAR(500),
    description TEXT,
    images TEXT,
    handler VARCHAR(50),
    mediation_record TEXT,
    agreement TEXT,
    result TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    create_time DATETIME
);

-- ============== 模块9: 人口户籍管理 ==============
CREATE TABLE IF NOT EXISTS population (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    household_name VARCHAR(50),
    id_card VARCHAR(50),
    gender VARCHAR(10),
    age INT,
    phone VARCHAR(20),
    address VARCHAR(200),
    family_members TEXT,
    is_permanent INT DEFAULT 1,
    work_location VARCHAR(200),
    group_name VARCHAR(50),
    create_time DATETIME
);

-- ============== 模块10: 土地宅基地管理 ==============
CREATE TABLE IF NOT EXISTS land_house (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    household_name VARCHAR(50),
    land_type VARCHAR(20),
    location VARCHAR(200),
    area DOUBLE DEFAULT 0,
    build_date DATETIME,
    owner_name VARCHAR(50),
    owner_id_card VARCHAR(50),
    status VARCHAR(20) DEFAULT 'NORMAL',
    contract_no VARCHAR(50),
    remark TEXT,
    create_time DATETIME
);

-- ============== 模块11: 惠民补贴发放 ==============
CREATE TABLE IF NOT EXISTS subsidy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subsidy_type VARCHAR(50),
    recipient_name VARCHAR(50),
    recipient_id_card VARCHAR(50),
    recipient_phone VARCHAR(20),
    bank_card VARCHAR(50),
    amount DOUBLE DEFAULT 0,
    issue_month VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    remark TEXT,
    create_time DATETIME
);

-- ============== 模块13: 志愿者管理 ==============
CREATE TABLE IF NOT EXISTS volunteer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    phone VARCHAR(20),
    address VARCHAR(200),
    service_type VARCHAR(100),
    join_date DATETIME,
    total_hours DOUBLE DEFAULT 0,
    points INT DEFAULT 0,
    status INT DEFAULT 1,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS volunteer_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    activity_type VARCHAR(50),
    content TEXT,
    location VARCHAR(200),
    activity_date DATETIME,
    participants TEXT,
    total_hours DOUBLE DEFAULT 0,
    images TEXT,
    create_time DATETIME
);

-- ============== 模块14: 项目工程管理 ==============
CREATE TABLE IF NOT EXISTS project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(200),
    project_type VARCHAR(20),
    content TEXT,
    total_amount DOUBLE DEFAULT 0,
    contractor VARCHAR(100),
    supervisor VARCHAR(100),
    start_date DATETIME,
    end_date DATETIME,
    progress INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PLANNING',
    images TEXT,
    remark TEXT,
    create_time DATETIME
);

-- ============== 模块15: 媒体宣传管理 ==============
CREATE TABLE IF NOT EXISTS media_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    article_type VARCHAR(20),
    content TEXT,
    images TEXT,
    author VARCHAR(50),
    publish_date DATETIME,
    status INT DEFAULT 1,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS media_submission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    target_media VARCHAR(100),
    content TEXT,
    submit_date DATETIME,
    publish_status VARCHAR(20) DEFAULT 'PENDING',
    publish_date DATETIME,
    remark TEXT,
    create_time DATETIME
);

-- ============== 模块18: 养老医保民政 ==============
CREATE TABLE IF NOT EXISTS pension_insurance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    person_name VARCHAR(50),
    id_card VARCHAR(50),
    phone VARCHAR(20),
    insurance_type VARCHAR(20),
    status VARCHAR(20) DEFAULT 'NORMAL',
    last_verify_date DATETIME,
    remark TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS civil_object (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    id_card VARCHAR(50),
    phone VARCHAR(20),
    object_type VARCHAR(20),
    family_members TEXT,
    address VARCHAR(200),
    subsidy_amount DOUBLE DEFAULT 0,
    status INT DEFAULT 1,
    remark TEXT,
    create_time DATETIME
);

-- ============== 模块19: 人居环境整治 ==============
CREATE TABLE IF NOT EXISTS environment_check (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    check_type VARCHAR(20),
    location VARCHAR(200),
    description TEXT,
    before_images TEXT,
    after_images TEXT,
    inspector VARCHAR(50),
    check_date DATETIME,
    status VARCHAR(20) DEFAULT 'PENDING',
    remark TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS cleaner (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    phone VARCHAR(20),
    area VARCHAR(200),
    attendance TEXT,
    salary DOUBLE DEFAULT 0,
    create_time DATETIME
);

-- ============== 模块20: 物资资产仓库 ==============
CREATE TABLE IF NOT EXISTS warehouse_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    quantity INT DEFAULT 0,
    unit VARCHAR(20),
    unit_price DOUBLE DEFAULT 0,
    storage_location VARCHAR(200),
    remark TEXT,
    create_time DATETIME
);

CREATE TABLE IF NOT EXISTS warehouse_flow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT,
    item_name VARCHAR(100),
    flow_type VARCHAR(20),
    quantity INT,
    applicant VARCHAR(50),
    purpose VARCHAR(500),
    flow_date DATETIME,
    return_date DATETIME,
    status VARCHAR(20) DEFAULT 'NORMAL',
    create_time DATETIME
);

-- ============== 测试数据: 新增模块 ==============
INSERT INTO petition (petition_type, petitioner_name, petitioner_phone, parties, description, handler, status, create_time) VALUES
('邻里纠纷', '张三', '13800138001', '张三、李四', '因宅基地边界产生纠纷', '王调解员', '已调解', NOW()),
('土地纠纷', '王五', '13800138003', '王某、村集体', '土地流转租金争议', '李调解员', '调解中', NOW());

INSERT INTO population (household_name, id_card, gender, age, phone, address, family_members, is_permanent, work_location, group_name, create_time) VALUES
('张大爷户', '430101195001011234', '男', 75, '13800138001', '幸福村1组', '张大爷、张大妈、张小三', 1, '', '第一组', NOW()),
('李叔叔户', '430101197501015678', '男', 50, '13800138002', '幸福村2组', '李叔叔、李婶、李小四', 0, '浙江杭州', '第二组', NOW());

INSERT INTO land_house (household_name, land_type, location, area, build_date, owner_name, owner_id_card, status, create_time) VALUES
('张大爷宅基', '宅基地', '幸福村1组', 120, '2005-06-01', '张大爷', '430101195001011234', 'NORMAL', NOW()),
('李叔叔承包地', '承包地', '幸福村南', 5.5, NULL, '李叔叔', '430101197501015678', 'NORMAL', NOW());

INSERT INTO subsidy (subsidy_type, recipient_name, recipient_id_card, recipient_phone, bank_card, amount, issue_month, status, create_time) VALUES
('耕地地力补贴', '张大爷', '430101195001011234', '13800138001', '6222021234567890', 500, '2024-06', 'ISSUED', NOW()),
('高龄补贴', '李奶奶', '430101193501019012', '13800138005', '6222020987654321', 200, '2024-06', 'PENDING', NOW());

INSERT INTO volunteer (name, gender, phone, address, service_type, join_date, total_hours, points, status, create_time) VALUES
('王小明', '男', '13800139001', '幸福村1组', '环境整治,政策宣传', '2024-01-15', 36, 180, 1, NOW()),
('李美丽', '女', '13800139002', '幸福村2组', '关爱老人,便民服务', '2024-02-20', 28, 140, 1, NOW());

INSERT INTO volunteer_activity (title, activity_type, content, location, activity_date, participants, total_hours, create_time) VALUES
('春季环境整治志愿活动', '环境整治', '全村卫生大扫除', '全村范围', '2024-03-15', '王小明,李美丽,张三', 24, NOW()),
('关爱独居老人活动', '关爱老人', '走访独居老人送温暖', '幸福村2组', '2024-04-10', '李美丽,王五', 8, NOW());

INSERT INTO project (project_name, project_type, content, total_amount, contractor, supervisor, start_date, end_date, progress, status, create_time) VALUES
('村道硬化工程', '道路工程', '村主干道硬化1.5公里', 350000, 'XX建筑公司', '监理张工', '2024-03-01', '2024-06-30', 80, 'IN_PROGRESS', NOW()),
('文化广场建设', '公共设施', '新建村民文化活动广场', 200000, 'YY建设公司', '监理李工', '2024-05-01', '2024-08-30', 30, 'IN_PROGRESS', NOW());

INSERT INTO media_article (title, article_type, content, author, publish_date, status, create_time) VALUES
('幸福村开展春季环境整治活动', '工作动态', '近日，幸福村组织全村党员干部开展春季环境卫生大整治活动', '村委宣传员', NOW(), 1, NOW()),
('幸福村茶叶产业助力乡村振兴', '产业宣传', '幸福村依托茶叶合作社带动村民增收致富', '村委宣传员', NOW(), 1, NOW());

INSERT INTO media_submission (title, target_media, content, submit_date, publish_status, create_time) VALUES
('幸福村美丽乡村建设纪实', '乡镇融媒体', '幸福村推进美丽乡村建设经验做法', NOW(), 'PUBLISHED', NOW()),
('幸福村党建引领基层治理', '县级媒体', '党建工作经验材料', NOW(), 'PENDING', NOW());

INSERT INTO pension_insurance (person_name, id_card, phone, insurance_type, status, last_verify_date, create_time) VALUES
('张大爷', '430101195001011234', '13800138001', '养老', 'VERIFIED', '2024-03-01', NOW()),
('李奶奶', '430101193501019012', '13800138005', '养老', 'PENDING', NULL, NOW());

INSERT INTO civil_object (name, id_card, phone, object_type, family_members, address, subsidy_amount, status, create_time) VALUES
('赵困难', '430101196501013456', '13800138006', '低保', '赵困难、妻、子', '幸福村3组', 600, 1, NOW()),
('钱残疾', '430101197001017890', '13800138007', '残疾人', '钱残疾', '幸福村1组', 300, 1, NOW());

INSERT INTO environment_check (check_type, location, description, inspector, check_date, status, create_time) VALUES
('垃圾乱堆', '村东路口', '发现建筑垃圾乱堆放', '王小明', '2024-03-20', '已整改', NOW()),
('污水排放', '村南水渠', '生活污水直排水渠', '李美丽', '2024-03-22', '整改中', NOW());

INSERT INTO cleaner (name, phone, area, attendance, salary, create_time) VALUES
('赵保洁', '13800138201', '村东片区', '全勤', 2500, NOW()),
('钱保洁', '13800138202', '村西片区', '请假2天', 2300, NOW());

INSERT INTO warehouse_item (name, category, quantity, unit, unit_price, storage_location, create_time) VALUES
('编织袋', '防汛物资', 500, '条', 1.5, '村委会仓库', NOW()),
('灭火器', '消防器材', 20, '个', 85, '村委会仓库', NOW()),
('打印纸', '办公用品', 50, '包', 25, '村委办公室', NOW());

INSERT INTO warehouse_flow (item_id, item_name, flow_type, quantity, applicant, purpose, flow_date, status, create_time) VALUES
(1, '编织袋', 'OUT', 50, '张三', '防汛应急使用', NOW(), 'NORMAL', NOW()),
(3, '打印纸', 'OUT', 5, '李四', '日常办公使用', NOW(), 'NORMAL', NOW());
