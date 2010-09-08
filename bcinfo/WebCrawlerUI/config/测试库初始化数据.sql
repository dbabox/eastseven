insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(0,'频道',0);
--频道
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(1,'新闻',0);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(2,'国际新闻',1);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(3,'国内新闻',1);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(4,'社会新闻',1);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(5,'四川新闻',1);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(6,'江西新闻',1);

insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(7,'体育',0);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(8,'NBA',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(9,'CBA',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(10,'英超',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(11,'意甲',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(12,'西甲',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(13,'中超',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(14,'国内',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(15,'国际',7);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(16,'综合',7);

insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(17,'军事',0);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(18,'国际军事',17);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(19,'国内军事',17);
       
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(20,'娱乐',0); 
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(21,'港台',20);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(22,'内地',20);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(23,'欧美',20);
insert into TWAP_PUBLIC_CHANNEL(CHANNEL_ID,CHANNEL_NAME,CHANNEL_PID) values(24,'日韩',20);

--抓取列表

--人员表
insert into twap_public_user(user_id,user_name,user_password,local_code) values(0,'admin','admin','0000');
--insert into twap_public_user(user_id,user_name,user_password,local_code) values(seq_twap_public_user.nextval,'sc','sc','028');
--insert into twap_public_user(user_id,user_name,user_password,local_code) values(seq_twap_public_user.nextval,'jx','jx','0791');

commit;
