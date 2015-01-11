from django.db import models
from django.conf import settings
from django.contrib.auth.models import AbstractUser
from django.contrib.auth.models import User
# Create your models here.
class MyUser(AbstractUser):
    utype = models.CharField(max_length=30)
    avatar = models.ImageField(upload_to='images',max_length=1000)
    

class Student(models.Model):
    stud_id = models.IntegerField()
    name = models.CharField(max_length=30)
    password = models.CharField(max_length=20)
    def __unicode__(self):
        return self.name

class Teacher(models.Model):
    teach_id = models.IntegerField()
    name = models.CharField(max_length=30)
    password=models.CharField(max_length=20)
    def __unicode__(self):
        return self.name

class Course(models.Model):
    course_id = models.IntegerField(default=1401)
    name = models.CharField(max_length=30,unique=True)
    teacher = models.IntegerField()
    number_student = models.IntegerField(default=0)
    number_project = models.IntegerField(default=0)
    number_test = models.IntegerField(default=0)
    status = models.IntegerField(default=-1)
    def __unicode__(self):
        return self.course_name

class stud_course_entry(models.Model):
    course = models.IntegerField()
    student = models.IntegerField()

class Group(models.Model):
    group_id = models.IntegerField()
    group_name = models.CharField(max_length=30)
    group_student = models.IntegerField()
    group_course = models.IntegerField()

class Sign(models.Model):
    student = models.IntegerField()
    course = models.IntegerField()
    time = models.DateTimeField(auto_now_add=True)

class Answer(models.Model):
    ans_id = models.IntegerField()
    ans_student_id = models.IntegerField()
    ans_question_id = models.IntegerField()
    ans_course_id = models.IntegerField()
    ans_test_id = models.IntegerField()
    ans_text = models.CharField(max_length=100)

class Question(models.Model):
    qst_id = models.IntegerField()
    qst_code = models.IntegerField()
    qst_content = models.CharField(max_length=100)
    qst_student = models.IntegerField()
    qst_course = models.IntegerField()

class Test(models.Model):
    id = models.AutoField(primary_key=True)
    tid = models.IntegerField(default=1)
    name = models.CharField(max_length=30)
    question_number= models.IntegerField(default=1)
    course = models.IntegerField(default=-1)
    status = models.IntegerField(default=-1)
    files = models.FileField(upload_to='static/files/test/')
    file_url = models.CharField(max_length=30)
    number_student = models.IntegerField(default=0)
    def __unicode__(self):
	return self.name

class Project(models.Model):
    pid = models.IntegerField(default=0)
    name = models.CharField(max_length=30)
    course = models.IntegerField()
    group_number = models.IntegerField(default=0)
    is_grouped = models.BooleanField(default=False)
    status = models.IntegerField(default=-1)
    files = models.FileField(upload_to='static/files/project')
    file_url = models.CharField(max_length=30)
    number_student = models.IntegerField(default=0)
    def __unicode__(self):
	return self.name

class Project_Question(models.Model):
	qid = models.IntegerField(default=-1)
	pid = models.IntegerField(default=-1)
	name=models.CharField(max_length=30)
	max_group=models.IntegerField(default=-1)
	number_per_group=models.IntegerField(default=-1)
	files = models.FileField(upload_to = 'static/files/project')
	file_url = models.CharField(max_length=30)
	first_number = models.IntegerField(default=0)
	second_number = models.IntegerField(default=0)
	third_number = models.IntegerField(default=0)
	def __unicode__(self):
		return self.name  

class Test_Answer(models.Model):
	tid = models.IntegerField(default = -1)
	aid = models.IntegerField(default = -1)
	answer = models.CharField(max_length=20)

class Student_Test_Answer(models.Model):
	tid = models.IntegerField(default=-1)
	sid = models.IntegerField(default=-1)
	aid = models.IntegerField(default=-1)
	answer = models.CharField(max_length=10)
	outcome = models.IntegerField(default=0)

class Groupinfo_Question(models.Model):
	pid = models.IntegerField(default=0)
	sid = models.IntegerField(default=0)
	qid = models.IntegerField(default=0)
	qname=models.CharField(max_length=30)
	priority=models.IntegerField(default=-1)
	
class Groupinfo_Partner(models.Model):
	pid = models.IntegerField(default=0)
	sid = models.IntegerField(default=0)
	partner = models.IntegerField(default=0)

class Group_Outcome(models.Model):
	pid = models.IntegerField(default=0)
	gid = models.IntegerField(default=0)
	pid = models.IntegerField(default=0)
	pname = models.CharField(max_length=30)
	sid = models.IntegerField(default=0)
	
