INSERT INTO [VU360USER] ([ID],[ACCEPTEDEULATF],[ACCOUNTNONEXPIREDTF],[ACCOUNTNONLOCKEDTF],[CHANGEPASSWORDONLOGINTF],[CREATEDDATE],[CREDENTIALSNONEXPIREDTF],[DOMAIN],[EMAILADDRESS],[ENABLEDTF],[FIRSTNAME],[LASTLOGONDATE],[LASTNAME],[LASTUPDATEDDATE],[NEWUSERTF],[NUMLOGONS],[PASSWORD],[USERNAME])VALUES(1,0,1,1,0,'Nov 12 2008  9:27:06:763PM',1,NULL,'jason.burns@360training.com',1,'Jason','Nov 17 2008  2:29:45:250PM','Burns','Nov 12 2008  9:27:06:763PM',0,2,'admin','jason')

INSERT INTO [brand] ([BRANDID],[BRANDKEY],[NAME])VALUES(1,'xxx','tathya')
INSERT INTO [brand] ([BRANDID],[BRANDKEY],[NAME])VALUES(2,'yyy','tathyaDotCom')

INSERT INTO [ORGANIZATIONALGROUP] ([ORGANIZATIONALGROUPID],[NAME],[PARENTORGGROUP_ID],[ROOTORGGROUP_ID])VALUES(2,'SUPER TATHYA',NULL,NULL)

INSERT INTO [ORGANIZATIONALGROUP] ([ORGANIZATIONALGROUPID],[NAME],[PARENTORGGROUP_ID],[ROOTORGGROUP_ID])VALUES(1,'Tathya',2,2)

INSERT INTO [LANGUAGE] ([LANGUAGEID],[COUNTRY],[LANGUAGE],[VARIANT])VALUES(1,'India','English','xx')
INSERT INTO [customer] ([CUSTOMERID],[BRAND_ID],[NAME],[STREETADDRESS],[CITY],[ZIPCODE],[EMAIL],[CONTACTPERSON],[STATUS],[BILLINGADDRESSID],[SHIPPINGADDRESSID],[DISTRIBUTORID],[ORGANIZATIONALGROUPID])VALUES(1,1,'Tathya','VIP Road',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)
INSERT INTO [learner] ([LEARNERID],[VU360USER_ID],[LEARNERNAME],[EMAIL],[ADDRESS],[CITY],[PROVINCE/STATE],[POSTALCODE/ZIP],[CUSTOMER_ID],[ORGANIZATIONALGROUPID])VALUES(1,1,'Tapas','tapas@tathya.com',NULL,NULL,NULL,NULL,1,1)

INSERT INTO [course] ([COURSEID],[GUID],[LANGUAGE_ID],[NAME],[VERSION],[COURSESTATUS],[RETIRED],[KEYWORDS],[CONTENTOWNER_ID],[COURSETYPE],[CODE],[DESCRIPTION],[IMAGEOFCOURSE],[APPROVEDCOURSEHOURS],[COURSTYPE_CATEGORY],[DELIVERYMETHOD],[CEUS],[LEARNINGOBJECTIVES],[TOPICSCOVERED],[QUIZINFO],[FINALEXAMINFO],[COURSEPRE_REQ],[STATE_REGREQ],[ENDOFCOURSEINSTRUCTIONS],[APPROVALNUMBER],[ADDITIONALMATERIALS],[SUBJECTMATTEREXPERTINFO],[PRODUCTPRICE],[CURRENCY],[ORGANIZATIONALGROUPID],[ORGANIZATIONALGROUPENTITLEMENTID])VALUES(1,'1',1,'MSc','1','1',1,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)

--Customer Entitlement

insert into [CUSTOMERENTITLEMENT](
	[DISTRIBUTORID] ,
	[CUSTOMERID] ,
	[COURSEID] ,
	[CUSTOMERENTITLEMENTID] ,
	[SEATS] 
)
values (1,1,1,1,10)

--DISTRIBUTORENTITLEMENT
insert into DISTRIBUTORENTITLEMENT values(1)

-- COURSEGROUP
insert into [COURSEGROUP](
	[COURSEGROUPID],
	[NAME] ,
	[PARENTCOURSEGROUP_ID],
	[CUSTOMERID] ,
	[CUSTOMERENTITLEMENTID],
	[DISTRIBUTORENTITLEMENTID],
	ROOTCOURSEGROUP_ID )
values (1,'Masters',1,1,1,1,null)

--LEARNERENROLLMENT

Insert into .[LEARNERENROLLMENT](
	[LEARNERENROLLMENTID] ,
	[ENROLLMENTDATE],
	[COURSESTARTDATE],
	[ENROLLMENTSTATUS],
	[CUSTOMERENTITLEMENTID],
	[LEARNERID] ,
	[COURSEID] ,
	[COURSEGROUPID] )
values (1,getdate(),'2008-12-12','NOT_STARTRD',1,1,1,1)

-- LEARNERGROUP
Insert into .LEARNERGROUP(
	LEARNERGROUPID ,
	CUSTOMERID,
	NAME,
	ORGANIZATIONALGROUPID)
values (1,1,'IT',1)


---ADDRESS
insert into [ADDRESS](
	[ADDRESSID] ,
	[STREETADDRESS] ,
	[CITY] ,
	[ZIPCODE],
	[STATE] )
values(1,'NAZRUL ISLAM Ave','Kolkata','700059','WB')

insert into [ADDRESS](
	[ADDRESSID] ,
	[STREETADDRESS] ,
	[CITY] ,
	[ZIPCODE],
	[STATE] )
values(2,'VIP ROAD','Kolkata','700059','WB')
--LEARNERPROFILE
insert into LEARNERPROFILE (LEARNERPROFILEID,LEARNERID,ADDRESSID)
values(1,1,1)

--LMSADMINISTRATOR
insert into LMSADMINISTRATOR (LMSADMINISTRATORID,GLOBALADMINISTRATORTF,VU360USER_ID) values(1,1,1)


--ORGANIZATIONALGROUPENTITLEMENT
insert into ORGANIZATIONALGROUPENTITLEMENT 
(ORGANIZATIONALGROUPENTITLEMENTID,ORGANIZATIONALGROUPID,CUSTOMERENTITLEMENTID)
Values(1,1,1)




insert into [COURSEGROUP](
	[COURSEGROUPID],
	[NAME] ,
	[PARENTCOURSEGROUP_ID],
	[CUSTOMERID] ,
	[CUSTOMERENTITLEMENTID],
	[DISTRIBUTORENTITLEMENTID],
	ROOTCOURSEGROUP_ID )
values (3,'Real Estate',null,1,1,1,3)

insert into [COURSEGROUP](
	[COURSEGROUPID],
	[NAME] ,
	[PARENTCOURSEGROUP_ID],
	[CUSTOMERID] ,
	[CUSTOMERENTITLEMENTID],
	[DISTRIBUTORENTITLEMENTID],
	ROOTCOURSEGROUP_ID )
values (4,'Pre-Licensing',3,1,1,1,3)

insert into [COURSEGROUP](
	[COURSEGROUPID],
	[NAME] ,
	[PARENTCOURSEGROUP_ID],
	[CUSTOMERID] ,
	[CUSTOMERENTITLEMENTID],
	[DISTRIBUTORENTITLEMENTID],
	ROOTCOURSEGROUP_ID )
values (5,'All States',4,1,1,1,3)


INSERT INTO [COURSE]
           ([COURSEID]
           ,[GUID]
           ,[LANGUAGE_ID]
           ,[NAME]
           ,[VERSION]
           ,[COURSESTATUS]
           ,[RETIRED]
           ,[KEYWORDS]
           ,[CONTENTOWNER_ID]
           ,[COURSETYPE]
           ,[CODE]
           ,[DESCRIPTION]
           ,[IMAGEOFCOURSE]
           ,[APPROVEDCOURSEHOURS]
           ,[COURSTYPE_CATEGORY]
           ,[DELIVERYMETHOD]
           ,[CEUS]
           ,[LEARNINGOBJECTIVES]
           ,[TOPICSCOVERED]
           ,[QUIZINFO]
           ,[FINALEXAMINFO]
           ,[COURSEPRE_REQ]
           ,[STATE_REGREQ]
           ,[ENDOFCOURSEINSTRUCTIONS]
           ,[APPROVALNUMBER]
           ,[ADDITIONALMATERIALS]
           ,[SUBJECTMATTEREXPERTINFO]
           ,[PRODUCTPRICE]
           ,[CURRENCY]
           ,[ORGANIZATIONALGROUPID]
           ,[ORGANIZATIONALGROUPENTITLEMENTID])
     VALUES
           (2
           ,123
           ,1
           ,'NEC 2008 Wiring and Protection'
           ,'1.1'
           ,'notstarted'
           ,0
           ,'NEC'
           ,null
           ,null
           ,null
           ,'DESC NEC 2008 Wiring and Protection'
           ,'IMAGEOFCOURSE NEC 2008 Wiring and Protection'
           ,10
           ,'COURSTYPE_CATEGORY NEC 2008 Wiring and Protection'
           ,'online'
           ,1
           ,'LEARNINGOBJECTIVES NEC 2008 Wiring and Protection'
           ,'TOPICSCOVERED NEC 2008 Wiring and Protection'
           ,'QUIZINFO NEC 2008 Wiring and Protection'
           ,'FINALEXAMINFO NEC 2008 Wiring and Protection'
           ,'COURSEPRE_REQ NEC 2008 Wiring and Protection'
           ,'STATE_REGREQ NEC 2008 Wiring and Protection'
           ,'ENDOFCOURSEINSTRUCTIONS NEC 2008 Wiring and Protection'
           ,'APPROVALNUMBER NEC 2008 Wiring and Protection'
           ,'ADDITIONALMATERIALS NEC 2008 Wiring and Protection'
           ,'SUBJECTMATTEREXPERTINFO NEC 2008 Wiring and Protection'
           ,100
           ,'$'
           ,1
           ,1)




INSERT INTO [lms_dev].[dbo].[COURSE]
           ([COURSEID]
           ,[GUID]
           ,[LANGUAGE_ID]
           ,[NAME]
           ,[VERSION]
           ,[COURSESTATUS]
           ,[RETIRED]
           ,[KEYWORDS]
           ,[CONTENTOWNER_ID]
           ,[COURSETYPE]
           ,[CODE]
           ,[DESCRIPTION]
           ,[IMAGEOFCOURSE]
           ,[APPROVEDCOURSEHOURS]
           ,[COURSTYPE_CATEGORY]
           ,[DELIVERYMETHOD]
           ,[CEUS]
           ,[LEARNINGOBJECTIVES]
           ,[TOPICSCOVERED]
           ,[QUIZINFO]
           ,[FINALEXAMINFO]
           ,[COURSEPRE_REQ]
           ,[STATE_REGREQ]
           ,[ENDOFCOURSEINSTRUCTIONS]
           ,[APPROVALNUMBER]
           ,[ADDITIONALMATERIALS]
           ,[SUBJECTMATTEREXPERTINFO]
           ,[PRODUCTPRICE]
           ,[CURRENCY]
           ,[ORGANIZATIONALGROUPID]
           ,[ORGANIZATIONALGROUPENTITLEMENTID])
     VALUES
           (3
           ,1234
           ,1
           ,'NEC 2008 Code Updates'
           ,'1.2'
           ,'inprogress'
           ,0
           ,'NEC'
           ,null
           ,null
           ,null
           ,'DESC NEC 2008 Code Updates'
           ,'IMAGEOFCOURSE NEC 2008 Code Updates'
           ,12
           ,'COURSTYPE_CATEGORY NEC 2008 Code Updates'
           ,'online'
           ,1
           ,'LEARNINGOBJECTIVES NEC 2008 Code Updates'
           ,'TOPICSCOVERED NEC 2008 Code Updates'
           ,'QUIZINFO NEC 2008 Code Updates'
           ,'FINALEXAMINFO NEC 2008 Code Updates'
           ,'COURSEPRE_REQ NEC 2008 Code Updates'
           ,'STATE_REGREQ NEC 2008 Code Updates'
           ,'ENDOFCOURSEINSTRUCTIONS NEC 2008 Code Updates'
           ,'APPROVALNUMBER NEC 2008 Code Updates'
           ,'ADDITIONALMATERIALS NEC 2008 Code Updates'
           ,'SUBJECTMATTEREXPERTINFO NEC 2008 Code Updates'
           ,200
           ,'$'
           ,1
           ,1)




INSERT INTO [lms_dev].[dbo].[COURSE]
           ([COURSEID]
           ,[GUID]
           ,[LANGUAGE_ID]
           ,[NAME]
           ,[VERSION]
           ,[COURSESTATUS]
           ,[RETIRED]
           ,[KEYWORDS]
           ,[CONTENTOWNER_ID]
           ,[COURSETYPE]
           ,[CODE]
           ,[DESCRIPTION]
           ,[IMAGEOFCOURSE]
           ,[APPROVEDCOURSEHOURS]
           ,[COURSTYPE_CATEGORY]
           ,[DELIVERYMETHOD]
           ,[CEUS]
           ,[LEARNINGOBJECTIVES]
           ,[TOPICSCOVERED]
           ,[QUIZINFO]
           ,[FINALEXAMINFO]
           ,[COURSEPRE_REQ]
           ,[STATE_REGREQ]
           ,[ENDOFCOURSEINSTRUCTIONS]
           ,[APPROVALNUMBER]
           ,[ADDITIONALMATERIALS]
           ,[SUBJECTMATTEREXPERTINFO]
           ,[PRODUCTPRICE]
           ,[CURRENCY]
           ,[ORGANIZATIONALGROUPID]
           ,[ORGANIZATIONALGROUPENTITLEMENTID])
     VALUES
           (4
           ,4455
           ,1
           ,'OSHA 10 Hour Construction Course'
           ,'1.4'
           ,'locked'
           ,0
           ,'OSHA'
           ,null
           ,null
           ,null
           ,'DESC OSHA 10 Hour Construction Course'
           ,'IMAGEOFCOURSE OSHA 10 Hour Construction Course'
           ,14
           ,'COURSTYPE_CATEGORY OSHA 10 Hour Construction Course'
           ,'online'
           ,1
           ,'LEARNINGOBJECTIVES OSHA 10 Hour Construction Course'
           ,'TOPICSCOVERED OSHA 10 Hour Construction Course'
           ,'QUIZINFO OSHA 10 Hour Construction Course'
           ,'FINALEXAMINFO OSHA 10 Hour Construction Course'
           ,'COURSEPRE_REQ OSHA 10 Hour Construction Course'
           ,'STATE_REGREQ OSHA 10 Hour Construction Course'
           ,'ENDOFCOURSEINSTRUCTIONS OSHA 10 Hour Construction Course'
           ,'APPROVALNUMBER OSHA 10 Hour Construction Course'
           ,'ADDITIONALMATERIALS OSHA 10 Hour Construction Course'
           ,'SUBJECTMATTEREXPERTINFO OSHA 10 Hour Construction Course'
           ,250
           ,'$'
           ,1
           ,1)
	



INSERT INTO [lms_dev].[dbo].[COURSE]
           ([COURSEID]
           ,[GUID]
           ,[LANGUAGE_ID]
           ,[NAME]
           ,[VERSION]
           ,[COURSESTATUS]
           ,[RETIRED]
           ,[KEYWORDS]
           ,[CONTENTOWNER_ID]
           ,[COURSETYPE]
           ,[CODE]
           ,[DESCRIPTION]
           ,[IMAGEOFCOURSE]
           ,[APPROVEDCOURSEHOURS]
           ,[COURSTYPE_CATEGORY]
           ,[DELIVERYMETHOD]
           ,[CEUS]
           ,[LEARNINGOBJECTIVES]
           ,[TOPICSCOVERED]
           ,[QUIZINFO]
           ,[FINALEXAMINFO]
           ,[COURSEPRE_REQ]
           ,[STATE_REGREQ]
           ,[ENDOFCOURSEINSTRUCTIONS]
           ,[APPROVALNUMBER]
           ,[ADDITIONALMATERIALS]
           ,[SUBJECTMATTEREXPERTINFO]
           ,[PRODUCTPRICE]
           ,[CURRENCY]
           ,[ORGANIZATIONALGROUPID]
           ,[ORGANIZATIONALGROUPENTITLEMENTID])
     VALUES
           (5
           ,4665
           ,1
           ,'OSHA 30 Hour Construction Course'
           ,'1.5'
           ,'completed'
           ,0
           ,'OSHA'
           ,null
           ,null
           ,null
           ,'DESC OSHA 30 Hour Construction Course'
           ,'IMAGEOFCOURSE OSHA 30 Hour Construction Course'
           ,30
           ,'COURSTYPE_CATEGORY OSHA 30 Hour Construction Course'
           ,'online'
           ,1
           ,'LEARNINGOBJECTIVES OSHA 30 Hour Construction Course'
           ,'TOPICSCOVERED OSHA 30 Hour Construction Course'
           ,'QUIZINFO OSHA 30 Hour Construction Course'
           ,'FINALEXAMINFO OSHA 30 Hour Construction Course'
           ,'COURSEPRE_REQ OSHA 30 Hour Construction Course'
           ,'STATE_REGREQ OSHA 30 Hour Construction Course'
           ,'ENDOFCOURSEINSTRUCTIONS OSHA 30 Hour Construction Course'
           ,'APPROVALNUMBER OSHA 30 Hour Construction Course'
           ,'ADDITIONALMATERIALS OSHA 30 Hour Construction Course'
           ,'SUBJECTMATTEREXPERTINFO OSHA 30 Hour Construction Course'
           ,350
           ,'$'
           ,1
           ,1)


INSERT INTO [COURSE_COURSEGROUP]
           ([COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (1
           ,5)


INSERT INTO [COURSE_COURSEGROUP]
           ([COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (2
           ,5)

INSERT INTO [COURSE_COURSEGROUP]
           ([COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (3
           ,5)

INSERT INTO [COURSE_COURSEGROUP]
           ([COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (4
           ,5)

INSERT INTO [COURSE_COURSEGROUP]
           ([COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (5
           ,5)





INSERT INTO [CUSTOMERENTITLEMENT]
           ([DISTRIBUTORID]
           ,[CUSTOMERID]
           ,[COURSEID]
           ,[CUSTOMERENTITLEMENTID]
           ,[SEATS])
     VALUES
           (1
           ,1
           ,2
           ,2
           ,15)


INSERT INTO [CUSTOMERENTITLEMENT]
           ([DISTRIBUTORID]
           ,[CUSTOMERID]
           ,[COURSEID]
           ,[CUSTOMERENTITLEMENTID]
           ,[SEATS])
     VALUES
           (1
           ,1
           ,3
           ,3
           ,7)



INSERT INTO [CUSTOMERENTITLEMENT]
           ([DISTRIBUTORID]
           ,[CUSTOMERID]
           ,[COURSEID]
           ,[CUSTOMERENTITLEMENTID]
           ,[SEATS])
     VALUES
           (1
           ,1
           ,4
           ,4
           ,8)


INSERT INTO [CUSTOMERENTITLEMENT]
           ([DISTRIBUTORID]
           ,[CUSTOMERID]
           ,[COURSEID]
           ,[CUSTOMERENTITLEMENTID]
           ,[SEATS])
     VALUES
           (1
           ,1
           ,5
           ,5
           ,5)




INSERT INTO [LEARNERENROLLMENT]
           ([LEARNERENROLLMENTID]
           ,[ENROLLMENTDATE]
           ,[COURSESTARTDATE]
           ,[ENROLLMENTSTATUS]
           ,[CUSTOMERENTITLEMENTID]
           ,[LEARNERID]
           ,[COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (2
           ,getdate()
           ,'2008-12-02'
           ,'active'
           ,2
           ,1
           ,2
           ,5)


INSERT INTO [LEARNERENROLLMENT]
           ([LEARNERENROLLMENTID]
           ,[ENROLLMENTDATE]
           ,[COURSESTARTDATE]
           ,[ENROLLMENTSTATUS]
           ,[CUSTOMERENTITLEMENTID]
           ,[LEARNERID]
           ,[COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (3
           ,'2008-11-01'
           ,'2008-11-10'
           ,'active'
           ,3
           ,1
           ,3
           ,5)


INSERT INTO [LEARNERENROLLMENT]
           ([LEARNERENROLLMENTID]
           ,[ENROLLMENTDATE]
           ,[COURSESTARTDATE]
           ,[ENROLLMENTSTATUS]
           ,[CUSTOMERENTITLEMENTID]
           ,[LEARNERID]
           ,[COURSEID]
           ,[COURSEGROUPID])
     VALUES
           (4
           ,'2008-11-01'
           ,'2008-11-10'
           ,'active'
           ,5
           ,1
           ,5
           ,5)


INSERT INTO [lms_dev].[dbo].[LEARNERCOURSESTATISTICS]
           ([LEARNERID]
           ,[LEARNERENROLLMENTID]
           ,[AVERAGEPOSTTESTSCORE]
           ,[AVERAGEQUIZSCORE]
           ,[COMPLETED]
           ,[COMPLETIONDATE]
           ,[FIRSTACCESSDATE]
           ,[FIRSTPOSTTESTDATE]
           ,[FIRSTQUIZDATE]
           ,[HEIGHESTPOSTTESTSCORE]
           ,[HIGHESTQUIZSCORE]
           ,[LASTACCESSDATE]
           ,[LASTPOSTTESTDATE]
           ,[LASTQUIZDATE]
           ,[LAUNCHESACCRUED]
           ,[LOWESTPOSTTESTSCORE]
           ,[LOWESTQUIZSCORE]
           ,[NUMBERPOSTTESTSTAKEN]
           ,[NUMBERQUIZESTAKEN]
           ,[PERCENTCOMPLETE]
           ,[PRETESTDATE]
           ,[PRETESTSCORE]
           ,[STATUS]
           ,[TOTALTIMEINSECONDS]
           ,[LEARNERCOURSESTATISTICSID])
     VALUES
           (1
           ,2
           ,null
           ,null
           ,0
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,'notstarted'
           ,0
           ,2)


INSERT INTO [lms_dev].[dbo].[LEARNERCOURSESTATISTICS]
           ([LEARNERID]
           ,[LEARNERENROLLMENTID]
           ,[AVERAGEPOSTTESTSCORE]
           ,[AVERAGEQUIZSCORE]
           ,[COMPLETED]
           ,[COMPLETIONDATE]
           ,[FIRSTACCESSDATE]
           ,[FIRSTPOSTTESTDATE]
           ,[FIRSTQUIZDATE]
           ,[HEIGHESTPOSTTESTSCORE]
           ,[HIGHESTQUIZSCORE]
           ,[LASTACCESSDATE]
           ,[LASTPOSTTESTDATE]
           ,[LASTQUIZDATE]
           ,[LAUNCHESACCRUED]
           ,[LOWESTPOSTTESTSCORE]
           ,[LOWESTQUIZSCORE]
           ,[NUMBERPOSTTESTSTAKEN]
           ,[NUMBERQUIZESTAKEN]
           ,[PERCENTCOMPLETE]
           ,[PRETESTDATE]
           ,[PRETESTSCORE]
           ,[STATUS]
           ,[TOTALTIMEINSECONDS]
           ,[LEARNERCOURSESTATISTICSID])
     VALUES
           (1--<LEARNERID, numeric,>
           ,3--<LEARNERENROLLMENTID, numeric,>
           ,34--<AVERAGEPOSTTESTSCORE, float,>
           ,40--<AVERAGEQUIZSCORE, float,>
           ,0--<COMPLETED, bit,>
           ,null--<COMPLETIONDATE, datetime,>
           ,'2008-11-13'--<FIRSTACCESSDATE, datetime,>
           ,'2008-11-14'--<FIRSTPOSTTESTDATE, datetime,>
           ,'2008-11-14'--<FIRSTQUIZDATE, datetime,>
           ,40--<HEIGHESTPOSTTESTSCORE, float,>
           ,40--<HIGHESTQUIZSCORE, float,>
           ,'2008-11-17'--<LASTACCESSDATE, datetime,>
           ,'2008-11-15'--<LASTPOSTTESTDATE, datetime,>
           ,'2008-11-15'--<LASTQUIZDATE, datetime,>
           ,12--<LAUNCHESACCRUED, numeric,>
           ,40--<LOWESTPOSTTESTSCORE, float,>
           ,40--<LOWESTQUIZSCORE, float,>
           ,2--<NUMBERPOSTTESTSTAKEN, numeric,>
           ,2--<NUMBERQUIZESTAKEN, float,>
           ,50--<PERCENTCOMPLETE, numeric,>
           ,'2008-11-13'--<PRETESTDATE, datetime,>
           ,39--<PRETESTSCORE, float,>
           ,'inprogress'--<STATUS, varchar(50),>
           ,100--<TOTALTIMEINSECONDS, numeric,>
           ,3)--<LEARNERCOURSESTATISTICSID, numeric,>)






	   INSERT INTO [lms_dev].[dbo].[LEARNERCOURSESTATISTICS]
           ([LEARNERID]
           ,[LEARNERENROLLMENTID]
           ,[AVERAGEPOSTTESTSCORE]
           ,[AVERAGEQUIZSCORE]
           ,[COMPLETED]
           ,[COMPLETIONDATE]
           ,[FIRSTACCESSDATE]
           ,[FIRSTPOSTTESTDATE]
           ,[FIRSTQUIZDATE]
           ,[HEIGHESTPOSTTESTSCORE]
           ,[HIGHESTQUIZSCORE]
           ,[LASTACCESSDATE]
           ,[LASTPOSTTESTDATE]
           ,[LASTQUIZDATE]
           ,[LAUNCHESACCRUED]
           ,[LOWESTPOSTTESTSCORE]
           ,[LOWESTQUIZSCORE]
           ,[NUMBERPOSTTESTSTAKEN]
           ,[NUMBERQUIZESTAKEN]
           ,[PERCENTCOMPLETE]
           ,[PRETESTDATE]
           ,[PRETESTSCORE]
           ,[STATUS]
           ,[TOTALTIMEINSECONDS]
           ,[LEARNERCOURSESTATISTICSID])
     VALUES
           (1--<LEARNERID, numeric,>
           ,4--<LEARNERENROLLMENTID, numeric,>
           ,34--<AVERAGEPOSTTESTSCORE, float,>
           ,40--<AVERAGEQUIZSCORE, float,>
           ,1--<COMPLETED, bit,>
           ,'2008-11-17'--<COMPLETIONDATE, datetime,>
           ,'2008-11-13'--<FIRSTACCESSDATE, datetime,>
           ,'2008-11-14'--<FIRSTPOSTTESTDATE, datetime,>
           ,'2008-11-14'--<FIRSTQUIZDATE, datetime,>
           ,40--<HEIGHESTPOSTTESTSCORE, float,>
           ,40--<HIGHESTQUIZSCORE, float,>
           ,'2008-11-17'--<LASTACCESSDATE, datetime,>
           ,'2008-11-15'--<LASTPOSTTESTDATE, datetime,>
           ,'2008-11-15'--<LASTQUIZDATE, datetime,>
           ,12--<LAUNCHESACCRUED, numeric,>
           ,40--<LOWESTPOSTTESTSCORE, float,>
           ,40--<LOWESTQUIZSCORE, float,>
           ,2--<NUMBERPOSTTESTSTAKEN, numeric,>
           ,2--<NUMBERQUIZESTAKEN, float,>
           ,50--<PERCENTCOMPLETE, numeric,>
           ,'2008-11-13'--<PRETESTDATE, datetime,>
           ,39--<PRETESTSCORE, float,>
           ,'completed'--<STATUS, varchar(50),>
           ,1000--<TOTALTIMEINSECONDS, numeric,>
           ,4)--<LEARNERCOURSESTATISTICSID, numeric,>)


update course set coursetype= 'Online' where courseid=1
update course set coursetype= 'Online' where courseid=2
update course set coursetype= 'PDF' where courseid=3
update course set coursetype= 'CD/DVD' where courseid=4
update course set coursetype= 'Course Group' where courseid=5
