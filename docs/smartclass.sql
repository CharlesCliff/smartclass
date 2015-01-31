-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2015-01-30 23:00:06
-- 服务器版本: 5.5.38-0ubuntu0.14.04.1
-- PHP 版本: 5.5.9-1ubuntu4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `smartclass`
--

-- --------------------------------------------------------

--
-- 表的结构 `auth_group`
--

CREATE TABLE IF NOT EXISTS `auth_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `auth_group_permissions`
--

CREATE TABLE IF NOT EXISTS `auth_group_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id` (`group_id`,`permission_id`),
  KEY `auth_group_permissions_0e939a4f` (`group_id`),
  KEY `auth_group_permissions_8373b171` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `auth_permission`
--

CREATE TABLE IF NOT EXISTS `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `content_type_id` int(11) NOT NULL,
  `codename` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `content_type_id` (`content_type_id`,`codename`),
  KEY `auth_permission_417f1b1c` (`content_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=76 ;

-- --------------------------------------------------------

--
-- 表的结构 `django_admin_log`
--

CREATE TABLE IF NOT EXISTS `django_admin_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_time` datetime NOT NULL,
  `object_id` longtext,
  `object_repr` varchar(200) NOT NULL,
  `action_flag` smallint(5) unsigned NOT NULL,
  `change_message` longtext NOT NULL,
  `content_type_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `django_admin_log_417f1b1c` (`content_type_id`),
  KEY `django_admin_log_e8701ad4` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `django_content_type`
--

CREATE TABLE IF NOT EXISTS `django_content_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `app_label` varchar(100) NOT NULL,
  `model` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `django_content_type_app_label_45f3b1d93ec8c61c_uniq` (`app_label`,`model`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=26 ;

-- --------------------------------------------------------

--
-- 表的结构 `django_migrations`
--

CREATE TABLE IF NOT EXISTS `django_migrations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `applied` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- 表的结构 `django_session`
--

CREATE TABLE IF NOT EXISTS `django_session` (
  `session_key` varchar(40) NOT NULL,
  `session_data` longtext NOT NULL,
  `expire_date` datetime NOT NULL,
  PRIMARY KEY (`session_key`),
  KEY `django_session_de54fa62` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_answer`
--

CREATE TABLE IF NOT EXISTS `smartClass_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ans_id` int(11) NOT NULL,
  `ans_student_id` int(11) NOT NULL,
  `ans_question_id` int(11) NOT NULL,
  `ans_course_id` int(11) NOT NULL,
  `ans_test_id` int(11) NOT NULL,
  `ans_text` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_course`
--

CREATE TABLE IF NOT EXISTS `smartClass_course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `teacher` int(11) NOT NULL,
  `number_student` int(11) NOT NULL,
  `number_project` int(11) NOT NULL,
  `number_test` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_group`
--

CREATE TABLE IF NOT EXISTS `smartClass_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `group_name` varchar(30) NOT NULL,
  `group_student` int(11) NOT NULL,
  `group_course` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_groupinfo_partner`
--

CREATE TABLE IF NOT EXISTS `smartClass_groupinfo_partner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `partner` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=75 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_groupinfo_question`
--

CREATE TABLE IF NOT EXISTS `smartClass_groupinfo_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `qid` int(11) NOT NULL,
  `qname` varchar(30) NOT NULL,
  `priority` int(11) NOT NULL,
  `priority_bak` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_group_outcome`
--

CREATE TABLE IF NOT EXISTS `smartClass_group_outcome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `gid` int(11) NOT NULL,
  `qid` int(11) NOT NULL,
  `qname` varchar(30) NOT NULL,
  `sid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=104 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_group_question_outcome`
--

CREATE TABLE IF NOT EXISTS `smartClass_group_question_outcome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `gid` int(11) NOT NULL,
  `qid` int(11) NOT NULL,
  `qname` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_group_student_outcome`
--

CREATE TABLE IF NOT EXISTS `smartClass_group_student_outcome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` int(11) NOT NULL,
  `gid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `sname` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_myuser`
--

CREATE TABLE IF NOT EXISTS `smartClass_myuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(128) NOT NULL,
  `last_login` datetime NOT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `username` varchar(30) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `email` varchar(75) NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `date_joined` datetime NOT NULL,
  `utype` varchar(30) NOT NULL,
  `avatar` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=58 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_myuser_groups`
--

CREATE TABLE IF NOT EXISTS `smartClass_myuser_groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `myuser_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `myuser_id` (`myuser_id`,`group_id`),
  KEY `smartClass_myuser_groups_f1d9e869` (`myuser_id`),
  KEY `smartClass_myuser_groups_5f412f9a` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_myuser_user_permissions`
--

CREATE TABLE IF NOT EXISTS `smartClass_myuser_user_permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `myuser_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `myuser_id` (`myuser_id`,`permission_id`),
  KEY `smartClass_myuser_user_permissions_f1d9e869` (`myuser_id`),
  KEY `smartClass_myuser_user_permissions_83d7f98b` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_project`
--

CREATE TABLE IF NOT EXISTS `smartClass_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `course` int(11) NOT NULL,
  `group_number` int(11) NOT NULL,
  `is_grouped` tinyint(1) NOT NULL,
  `status` int(11) NOT NULL,
  `files` varchar(100) NOT NULL,
  `file_url` varchar(30) NOT NULL,
  `number_student` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_project_question`
--

CREATE TABLE IF NOT EXISTS `smartClass_project_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `qid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `max_group` int(11) NOT NULL,
  `number_per_group` int(11) NOT NULL,
  `files` varchar(100) NOT NULL,
  `file_url` varchar(30) NOT NULL,
  `first_number` int(11) NOT NULL,
  `second_number` int(11) NOT NULL,
  `third_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_question`
--

CREATE TABLE IF NOT EXISTS `smartClass_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `qst_id` int(11) NOT NULL,
  `qst_code` int(11) NOT NULL,
  `qst_content` varchar(100) NOT NULL,
  `qst_student` int(11) NOT NULL,
  `qst_course` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_sign`
--

CREATE TABLE IF NOT EXISTS `smartClass_sign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student` int(11) NOT NULL,
  `course` int(11) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_student`
--

CREATE TABLE IF NOT EXISTS `smartClass_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stud_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_student_test`
--

CREATE TABLE IF NOT EXISTS `smartClass_student_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `accuracy` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_student_test_answer`
--

CREATE TABLE IF NOT EXISTS `smartClass_student_test_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `sid` int(11) NOT NULL,
  `aid` int(11) NOT NULL,
  `answer` varchar(10) NOT NULL,
  `outcome` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=56 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_stud_course_entry`
--

CREATE TABLE IF NOT EXISTS `smartClass_stud_course_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course` int(11) NOT NULL,
  `student` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=26 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_teacher`
--

CREATE TABLE IF NOT EXISTS `smartClass_teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `teach_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_test`
--

CREATE TABLE IF NOT EXISTS `smartClass_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `question_number` int(11) NOT NULL,
  `course` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `files` varchar(100) NOT NULL,
  `file_url` varchar(30) NOT NULL,
  `number_student` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- 表的结构 `smartClass_test_answer`
--

CREATE TABLE IF NOT EXISTS `smartClass_test_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `aid` int(11) NOT NULL,
  `answer` varchar(20) NOT NULL,
  `accuracy` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- 限制导出的表
--

--
-- 限制表 `auth_group_permissions`
--
ALTER TABLE `auth_group_permissions`
  ADD CONSTRAINT `auth_group_permission_group_id_689710a9a73b7457_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
  ADD CONSTRAINT `auth_group__permission_id_1f49ccbbdc69d2fc_fk_auth_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`);

--
-- 限制表 `auth_permission`
--
ALTER TABLE `auth_permission`
  ADD CONSTRAINT `auth__content_type_id_508cf46651277a81_fk_django_content_type_id` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);

--
-- 限制表 `django_admin_log`
--
ALTER TABLE `django_admin_log`
  ADD CONSTRAINT `django_admin_lo_user_id_52fdd58701c5f563_fk_smartClass_myuser_id` FOREIGN KEY (`user_id`) REFERENCES `smartClass_myuser` (`id`),
  ADD CONSTRAINT `djang_content_type_id_697914295151027a_fk_django_content_type_id` FOREIGN KEY (`content_type_id`) REFERENCES `django_content_type` (`id`);

--
-- 限制表 `smartClass_myuser_groups`
--
ALTER TABLE `smartClass_myuser_groups`
  ADD CONSTRAINT `myuser_id_refs_id_71014ea6` FOREIGN KEY (`myuser_id`) REFERENCES `smartClass_myuser` (`id`);

--
-- 限制表 `smartClass_myuser_user_permissions`
--
ALTER TABLE `smartClass_myuser_user_permissions`
  ADD CONSTRAINT `myuser_id_refs_id_baae3c9b` FOREIGN KEY (`myuser_id`) REFERENCES `smartClass_myuser` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
