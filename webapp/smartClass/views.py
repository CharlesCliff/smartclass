#coding=utf-8
from django.shortcuts import render,render_to_response,get_object_or_404
from django.http import HttpResponse,HttpResponseRedirect
from django.template import RequestContext
from models import Student,Teacher,Course,Sign,Answer,Group,stud_course_entry,MyUser,Test,Project,Project_Question
from models import Test_Answer,Student_Test_Answer,Groupinfo_Question,Groupinfo_Partner,Group_Outcome
import json
from django.views.decorators.csrf import csrf_exempt
import simplejson
from django.contrib.auth.models import User,Group,Permission
from django.contrib.auth import authenticate,login as user_login,logout as user_logout
from django.contrib.contenttypes.models import ContentType
from django import template
from django.template import Template,Context
from django.template.loader import get_template
from django import forms
from django.core.urlresolvers import reverse
from django.core.files.base import ContentFile
#表单
#test
def test(request):
    print 'sessionid is : ',request.session._session_key
    t =get_template('register.html')
#    form = ContactForm()
    c = RequestContext(request,{'title':'Register'})
    return HttpResponse(t.render(c))
#    return render_to_response('register.html',{'title':'Register'})
#    return HttpResponse("Hello Django")
##
@csrf_exempt
def testform(request):
     user = request.GET.get('username')
     passwd = request.GET.get('password')
     passwd2 = request.GET.get('password2')
     print user,':',passwd,':',passwd2
     return HttpResponse('form test')
#
#
#
#
@csrf_exempt
def login(request):
    print r'reached here\n'
    result = {}
    if request.method == 'POST' :
        print 'here in post'
        req = simplejson.loads(request.body)
        print req
        username = req['username']
        password = req['password']
        role = req['role']
        if 'student'==role :  #student login
            print 'role is student'
            user = Student.objects.filter(stud_id__exact = username,password__exact = password)    
            if user : #用户存在，返回登陆成功信息 
                result['successedlogin'] = 1
            elif not user  :
                result['successedlogin'] = 0
            print result
            json = simplejson.dumps(result)
            return HttpResponse(json)
        elif 'teacher'==role :  # teacher login 
            teach = Teacher.objects.filter(teach_id__exact = username , password__exact = password)
            if teach :
                    result['successedlogin'] = 1
            elif not  teach  :
                    result['successedlogin'] = 0
            json = simplejson.dumps(result)
            return HttpResponse(json)
##
#
#注册
#
#
		
@csrf_exempt
def register(request):
    result = {}
    if request.method == 'POST':
        print 'method is post'
        req = simplejson.loads(request.body)
        username = req['username']
        password = req['password']
        if 'student' == req['role'] :
            print 'role is student'
            user = Student.objects.filter(stud_id__exact = username)
            if user :    #用户已经存在
                result['successedregister']=0
                result['message'] = 'user already exist'
                print result['message']
                json = simplejson.dumps(result)
                return HttpResponse(json)
            elif  not user :
            #添加到数据库
                print 'not exist'
                entry = Student(stud_id = username,password = password)
                entry.save()
                print 'user saved'
                result['successedregister']=1
                result['message']='register successfully!'
                json = simplejson.dumps(result)
                return HttpResponse(json)
        elif 'teacher'==req['role'] :
            teach = Teacher.objects.filter(teach_id__exact = username)
            if teach : 
                result['successedregister']=0
		result['message']='teacher already registered'
                json = simplejson.dumps(result)
                return HttpResponse(json)
            elif not teach  : 
                entry = Teacher(teach_id = username,password = password)
                entry.save()
                result['successedregister']=1
                result['message']='register successfully!'
                json = simplejson.dumps(result)
                return HttpResponse(json)

##
#
#
#登陆成功
def index(req):
    return HttpResponse('Hello Django')
    #username = req.COOKIES.get('username','')
    #return render_to_response('index.html' ,{'username':username})

##
#
#
#提问
@csrf_exempt
def question(request):
    if request.method == 'POST' :
        req = simplejson.loads(request.body,strict=False)
        # get the json parameters,username,course,questionnumber and answer
        username = req['username']
        course = req['course']
        role = req['role']
        quetionnumber = req['questionnumber']
        answer = req['answer']
        #check the parameter validating
        #store to the db
        ans = Answer(question_id = questionnumber, course_id = course,text = answer)
        ans.save()
          
        
#def is_login(username):
    
#
#
#
#
#个人信息添加、修改、查询
@csrf_exempt
def studentInfo(request):
    if request.method is not 'POST':
        return HttpResponse("need Post")
    req = simplejson.loads(request.body,strict=False)
    #print 'get the body: ',req
    result = {}
#    username operation oldpasswd,newpasswd,newcourse        
    username = req['username']
    operation = req['operation']
    newcourse = req['newcourse']
    oldpasswd = req['oldpasswd']
    newpasswd = req['newpasswd']
    print 'username is ',username,' and operation is ',operation
    opt = ['querycourselist','addnewcourse','changepasswd']
    #querycourselist,addnewcourse,changepasswd
    if opt[0] == operation :     #查询所选课程
        print 'query courses'
        user = Student.objects.filter(stud_id__exact = username)
        if not user : #用户不存在，返回错误信息
            result['message']='user not exists'
            result['querysuccess'] ='nouser'
            json = simplejson.dumps(result)
            return HttpResponse(json)

            course = Course.objects.filter(course__in =  ccid)
            if not cid : #没有所选课程
                result['message']='no class found'
                result['querysuccess']='noclass'
                json = simplejson.dumps(result)
                return HttpResponse(json)
	    elif cid :
		course = []
   		teacher = []
                for entry in course :
                    course.append(entry.course_name)
		    teacher.append(entry.course_teacher)
                cstr = "".join(course)
                tstr = "".join(teacher)
                result['querysuccess']='success'
                result['courselist'] = cstr
                result['teacherlist'] = tstr
                json = simplejson.dumps(result)
                return HttpResponse(json)
    # addnewcourse
    elif opt[1] == operation :
        user = Student.objects.filter(stud_id__exact = username)
        if not user :
            result['addsuccess']='nouser'
            result['message'] = 'no user exist'
            json = simplejson.dumps(result)
            return HttpResponse(json)
        elif user : #添加课程
            #是否存在该课程
            course = Course.objects.filter(course__exact = newcourse)
            if not course : #不存在该课程
                result['addsuccess']='noclass'
                json = simplejson.dumps(result)
                return HttpResponse(json)
            elif course :
                c = stud_course_entry(course=newcourse,student = username)
                c.save()
                result['addsuccess']='success'
                result['message']='add course success'
                json = simplejson.dumps(result)
                return HttpResponse(json)
    # change password
    elif opt[2] == operation :
        user = Student.objects.filter(stud_id__exact = username,password = oldpasswd)
        if not user :
            result['changesuccess']='wrongpasswd'
            result['message'] = 'password is wrong'
            json = simplejson.dumps(result)
            return HttpResponse(json)
        elif user :
            u = Student.objects.get(stud_id=username)
            u.password = newpasswd
            u.save()
            result['changesuccess']='success'
            result['message']='change password success'
            json = simplejson.dumps(result)
            return HttpResponse(json)
#
#the following part is for the teacher to
# login,createClass and checkInfo
#
#
def teacherRegister_view(request):
    return render_to_response('register.html',{'title':'SmartClass Teacher'})

@csrf_exempt
def student_register(request):
	if request.method == 'POST':
		username = request.POST.get('username','')
		password = request.POST.get('password','')
		user = authenticate(username = username,password=password)
		if user is not None:
			print 'user is not none'
			return HttpResponse(json.dumps({'result':0,'message':'user already exists'}))
		else :
			print 'user is none'
			if username and password :
				print 'username is : ',username
				user = MyUser.objects.create_user(username = username,password=password,utype='student')
				user.is_active=True
				user.save
				return HttpResponse(json.dumps({'result':1,'message':'regster success'}))
	else :
		return HttpResponse('wrong method')
####student login
@csrf_exempt
def student_login(request):
	if request.method=='POST':
		username = request.POST.get('username','')
		password = request.POST.get('password','')
		print 'username : ',username ,' password: ',password
		user = authenticate(username=username,password=password)
		if user is not None :
			if user.is_active:
				print 'user active'
				user_login(request,user)
				return HttpResponse(json.dumps({'result':1,'message':'login success'}))
			else :
				return HttpResponse(json.dumps({'result':0,'message':'user not active'}))
		else :
			return HttpResponse(json.dumps({'result':0,'message':'user not exists or password is wrong'}))
	else :
		return HttpResponse('wrong password')


#get all course list
@csrf_exempt
def student_all_course(request):
	if request.method == 'POST':
#		print 'sessionid is : ',request.session._session_key
		clist =  Course.objects.all()
		cname = [c.name for c in clist]
#		for s in cname:
#			print s.encode('gb2312')
		response =  HttpResponse(json.dumps({'all_course_list':cname}))
		response.set_cookie('username','hello me')
		return response
#----------get student course:
@csrf_exempt
def student_my_course(request):
	if request.method == 'POST':
		user = request.user
	        	
		uid = user.id
			
		entry = stud_course_entry.objects.filter(student = uid)
		if len(entry):
			cids = [e.course for e in entry]
		#	for s in cids:
		#		print 'cid is: ',s
			cours = Course.objects.filter(id__in=cids)
			cname = [c.name for c in cours]
		#	for s in cname :
		#		print 'course: ',s
			return HttpResponse(json.dumps({'my_course_list':cname}))
		else :
			return HttpResponse(json.dumps({'my_course_list':[]}))

@csrf_exempt
def student_join_course(request):
	if request.method=='POST':
		cname = request.POST.get('coursename','')
		#print 'course name is : ' , cname
	        cour = Course.objects.filter(name=cname)
		if len(cour)==1:
			cid = cour[0].id
		#	print 'cid is : ',cid
		user = request.user
		uid = user.id
		sc = stud_course_entry.objects.filter(course = cid,student= uid)
		if len(sc):
			return HttpResponse(json.dumps({'result':0,'message':'already joined'}))
		else :
			entry = stud_course_entry(course=cid,student=uid)
			entry.save()
			return HttpResponse(json.dumps({'result':1,'message':'join course success'}))
	else :
		return HttpResponse('wrong method')


@csrf_exempt 
def student_course_test(request):
	if request.method=='POST':
		coursename = request.POST.get('coursename','')
		test = get_test(coursename)
		if test is  None:
			return HttpResponse('test is none')
		if len(test):
			tname = [(t.name,t.question_number) for t in test]
			#for t in tname:
			#	print "".join(t)
			return HttpResponse(json.dumps({'result':1,'message':'get test success','testlist':tname}))
		else:
			return HttpResponse(json.dumps({'result':0,'message':'test not exist'}))
	else:
		 return HttpResponse('wrong method')

@csrf_exempt
def student_course_project(request):
	if request.method == 'POST':
		cname = request.POST.get('coursename','')
		if cname :

			c = get_course_by_name(cname)
			if c:
				cid = c.id
				plist = get_project_by_course(cid)
				sce = stud_course_entry.objects.filter(course=c.id).order_by('student')
				sid = [t.student for t in sce]
				stdlist=MyUser.objects.filter(id__in=sid)
				studentlist = [t.username for t in stdlist]
				if plist:
					projectlist = [p.name for p in plist]
					return HttpResponse(json.dumps({'result':1,'message':'get project list success','projectlist':projectlist,'studentlist':studentlist}))
	return HttpResponse('error')


#--------get all the questions of the project by coursename and project name
@csrf_exempt
def student_project_question(request):
	if request.method == 'POST':
		cname = request.POST.get('coursename')
		pname = request.POST.get('projectname')
		print 'cname: ',cname,' , ',pname
		if cname and pname :
			c = get_course_by_name(cname)
			if c:
				print 'course is: ',c.id
				p = Project.objects.filter(name=pname,course=c.id)
				project = get_project_by_name(pname,c.id)
				if project:
					print 'project id is: ',project.id
				#if len(p):
				#	project = p[0]
					qlist = get_question_by_project(project.id)
					#qlist = Project_Question.objects.filter(pid=project.id)
					if len(qlist):
						print 'qlist is not none'
						pq = [[t.name,t.max_group,t.number_per_group] for t in qlist]
						print 'pq length: ',len(pq)
						return HttpResponse(json.dumps({'result':1,'message':'get project question success','questionlist':pq}))
	return HttpResponse('error')



@csrf_exempt
def student_test_answer(request):
	if request.method == 'POST':
		cname = request.POST.get('coursename','')
		tname = request.POST.get('testname','')
		user=request.user
		print 'user is : ',user.id
		if cname and tname :
			course = get_course_by_name(cname)
			if course:
				print 'course is: ',course.name
				test = get_test_by_name(tname,course.id)
				if test:
					print 'test is: ',test.id
					#check if the answer has been added
					ta = Student_Test_Answer.objects.filter(tid=test.id,sid=user.id)
					if len(ta):
						return HttpResponse(json.dumps({'result':1,'message':'answers has been added'}))
					print 'tid is:' ,test.id
					rightanswer = Test_Answer.objects.filter(tid=test.id).order_by('aid')
					if len(rightanswer)==0:
						print 'no right answer'
						return HttpResponse(json.dumps({'result':0,'message':'answers has been added'}))
					test_answer = [a.answer for a in rightanswer]
					print 'len of test_answer is: ',len(test_answer)
					i=0
					for i in range(0,len(test_answer)):
						outcome = 0
						key = "".join(["answer",str(i+1)])
						print 'key is: ',key
						aw = request.POST.get(key,'')
						print 'answeri is ',aw
						if aw==test_answer[i]:
							outcome = 1
						print 'userid is: ',user.id
						sid = user.id
						tid=test.id
						aid = i+1
						sta = Student_Test_Answer(tid = tid,sid=sid,aid=aid,answer=aw)
						sta.save()
					return HttpResponse(json.dumps({'result':1,'message':'save answer success'}))
				else:
					return HttpResponse(json.dumps({'result':0,'message':'test is none'}))
		else :
			return HttpResponse(json.dumps({'result':0,'message':'cname,tname, answer or user is none'}))
					

@csrf_exempt 
def student_project_groupinfo(request):
	if request.method == 'POST':
		req = json.loads(request.body)
		cname = req['coursename']
		pname = req['projectname']
		memberlist = req['memberlist']
		questionlist = req['questionlist']
		if cname and tname and memberlist and questionlist :
			course = get_course_by_name(cname)
			if course:
				cid = course.id
				project = get_project_by_name(pname,cid)
				if project :
					pid = project.id
					sid = request.user.id
					tmp = Groupinfo_Question.objects.filter(pid=pid,sid=sid)
					if len(tmp):
						return HttpResponse(json.dumps({'result':0,'message':'groupinfo already exist'}))
					for i in range(len(questionlist)):
						question = get_question_by_name(questionlist[i],pid)
						gq = Groupinfo_Question(pid=pid,sid=sid,qid=question.id,qname=question.name,priority=i+1)
						gq.save()
					for i in range(len(memberlist)):
						partner = MyUser.objects.get(username=memberlist[i])
						gpartner= Groupinfo_Partner(pid=pid,sid=sid,partner=partner.id)
						gpartner.save()
					return HttpResponse(json.dumps({'result':1,'message':'add groupinfo success'}))
				return HttpResponse({'result':0,'message':'project not exist'})
			return HttpResponse({'result':0,'message':'course not exist'})
		return HttpResponse({'result':0,'message':'parameter are none'})
	return HttpResponse({'result':0,'message':'request method is wrong'})
		
#-----------------
#
#teacher api part
#
@csrf_exempt
def teacher_register(request):
    username=None
    password=None
    password2=None
    errors=[]
    flag = False
    if request.method == 'GET':
	return render_to_response('register.html',RequestContext(request,{'title':'TeacherRegister'}))	
    elif request.method == 'POST' :
        username = request.POST.get('username','')
        password =request.POST.get('password','')
        password2 = request.POST.get('password2','')
        #user = MyUser.objects.filter(username = username)
        user = authenticate(username=username,password=password)
        if  user:
            errors.append("User Already exists.")
            c = RequestContext(request,{'title':'UserExists','errors':errors})
            return render_to_response('register.html',c)
        else :
            if not username:
                errors.append("Please Enter username.")
            if not password :
                errors.append('Please enter password.')
            if not password2 :
                errors.append('Please re-enter password.')
            if password is not None and password2 is not None:
                if password!=password2:
                    errors.append('password did not the same')
                else : 
                    flag=True
            if username and password and password2 and flag :
                user = MyUser.objects.create_user(username=username,password=password,utype='teacher')
                user.is_active=True
                user.save
                nuser = authenticate(username=username,password=password)
                user_login(request,nuser)
                request.session['user']=user.id
                request.session['type'] = 'teacher'
                return render_to_response('home.html',RequestContext(request,{'title':'TeacherHome'}))
            return render_to_response('register.html',{'title':'RegisterError','errors':errors})

            
        #check if the user already existed
        
@csrf_exempt
def teacher_login1(request):
    errors=[]
    if request.method=='POST':
	username = request.POST['username']
	password = request.POST['password']
        ##debug
#	print "username is :",username," password is : ",password
        user = MyUser.objects.get(username=username)
 #       print "user.password is : ",user.password.encode('utf8')
        if user is not None:
            if user.is_active:
                if user.password==password:
                    reqeust.session['user']=user.id
	            return HttpResponseRedirect('../home')
		else :
		    errors.append('password is wrong')
	    else :
		errors.append('user is not active')
	else :
	    errors.append('username does not exist')
	return render_to_response('login.html',RequestContext(request,{'title':'LoginError','errors':errors}))
    else:
	 return HttpResponse('method wrong')
     
    ##check if the user exists
    user = Teacher.objects.filter(teach_id__exact = username, password = password)
    return HttpResponseRedirect('/teacherIndex/')

@csrf_exempt
def teacher_login(request):
    errors=[]
    if request.method=='POST':
	username = request.POST['username']
	password = request.POST['password']
        ##debug
	#print "username is :",username," password is : ",password
        user = authenticate(username=username,password=password)
        if user is not None:
            if user.is_active:
                    user_login(request,user)
	#	    print "user logined: ",request.user.username.encode('gb2312')," type is : ",request.user.utype.encode('gb2312')
                    request.session['user']=user.id
	            return HttpResponseRedirect('../home')
	    else :
		errors.append('user is not active')
	else :
	    errors.append('username or password is wrong')
	return render_to_response('login.html',RequestContext(request,{'title':'LoginError','errors':errors}))
    elif request.method=='GET':
	return render_to_response('login.html',RequestContext(request,{'title':'TeacherLogin'}))
	 #return HttpResponse('method wrong')
     
#-----------teacher logout
@csrf_exempt
def teacher_logout(request):
	user = request.user
	#print 'user is : ',user.username
	user_logout(request)
	return HttpResponseRedirect(reverse('teacher_login'))

#------------------------
def myauthenticate(username,password):
    user = MyUser.objects.get(username=username,password=password)
    return user
def mylogin(request,user):
    request.session['user']=user.id
    return

@csrf_exempt
def teacherIndex(request):
    ##get the username for the session
    return HttpResponse('<h>Hello Teacher</h>')           

@csrf_exempt
def teacher_home(request):
    #get the courses the teacher teaches
    user=request.user
    #print "at home user is :",user.username
    course=Course.objects.filter(teacher=user.id)
  #  for c in course :
   #     print "course Name: ",c.name.encode('gb2312')
    return render_to_response('home.html',RequestContext(request,{'title':'TeacherHome','courses':course}))

@csrf_exempt
def create_course(request):
    if  request.method == 'POST':
        name=request.POST.get('course-name','')
     #   print "course is : ", name
	number = request.POST.get('max-number','')
        try:
            c = Course.objects.get(name__exact=name)
            if c:
	        message="Class Already Exist!"
	        return render_to_response('home.html',RequestContext(request,{'title':'TeacherHome','message':message}))

        except:
           print "course does not exists"
        user = request.user
        
	cou = Course(name=name,teacher=user.id)#,max_number=number)
        cou.save()
        #yield render_to_response('home.html',RequestContext(request,{'title':'TeacherHome','message':'Create Success!'}))
        return HttpResponseRedirect(reverse('teacher_home'))

@csrf_exempt
def teacher_course(request,course_name):
	cname = course_name
        #cname = request.GET.get('course','')
    #try:
        c = Course.objects.get(name=cname)
	cid = c.id
	#get the test of this course
	tests = Test.objects.filter(course = cid)
	#get the bighomework of the course
	project = Project.objects.filter(course= cid)
	request.session['course']=cname
	request.session['courseid']=cid
      #  print "the get corse is :",c.name.encode('gb2312')
        return render_to_response('course.html',RequestContext(request,{'title':'CourseHome','course':c,'tests':tests,'projects':project}))
    #except :
     #   return HttpResponse('course not exists')    

#-----------course/project
@csrf_exempt
def course_project(request,project_name):
	pname = project_name
	p = Project.objects.get(name=pname)
	request.session['project']=p.id
	request.session['project_name']=p.name
	questions = Project_Question.objects.filter(pid=p.id)
	c = RequestContext(request,
		{'title':'Project',
		 'project':p,
		 'questions':questions
		})
	return render_to_response('project.html',c)

#--------------------add question----
@csrf_exempt
def project_add_question(request):
	if request.method=='POST':
		qid = request.POST.get('question_id','')
		pid = request.session['project']
		qname = request.POST.get('question_name','')
		number_per_group = request.POST.get('per_number','')
		max_group = request.POST.get('max_group','')
		pad = Project_Question.objects.filter(name=qname)
		pname = request.session['project_name']
		if len(pad):
			return HttpResponse('question already exist')
		else :
			q = Project_Question(name=qname,qid=qid,pid=pid,number_per_group=number_per_group,max_group=max_group)
			q.save()
			return HttpResponseRedirect(reverse('course_project',args=(pname,)))
			
		
#--------course/test
@csrf_exempt
def course_test(request,test_name):
	tname = test_name
	print 'testname is : ',tname.encode('gb2312')
	cid = request.session['courseid']
	t = Test.objects.get(name=tname,course=cid)
	answer_range=range(1,t.question_number+1)
	test_answer = Test_Answer.objects.filter(tid=t.id).order_by('aid')
	#print 'testanswer is: ',len(test_answer)
	c = RequestContext(request,{'title':'Test','test':t,'answer_range':answer_range,'test_answer':test_answer})
	request.session['test']=t.id
	request.session['testname']=tname
	return render_to_response('testing.html',c)



@csrf_exempt
def create_test(request):
	c=None
	if request.method=='POST':
		testname = request.POST.get('testname','')
		number = request.POST.get('q_number','')
		if 'course' in request.session:
			c = Course.objects.get(name=request.session['course'])
		if c :
			t = Test(name=testname,course = c.id ,question_number=number)
			t.save()
			return HttpResponseRedirect(reverse('teacher_course',args=(c.name,)))
	else :
		return HttpResponse('wrong method')

#----------add answers-----
@csrf_exempt
def test_add_answer(request):
	if request.method=='POST':
		cid = request.session['course']
		tid = request.session['test']
		test = Test.objects.get(id=tid)
		tname = request.session['testname']
	#	print 'testname is: ',tname
		qnumber = test.question_number
		for i in range(1,qnumber+1):
			key = "".join(["answer_",str(i)])
	#		print 'key is: ',key
			aid = i
			answer = request.POST.get(key,'')
			ta = Test_Answer.objects.filter(tid=tid,aid=aid)
			if len(ta):
				return HttpResponse('answer %d already exists' % i)
			else :
				temp = Test_Answer(tid = tid,aid=aid,answer=answer)
				temp.save()
		return HttpResponseRedirect(reverse('course_test',args=(tname,)))


#-------------create_project
@csrf_exempt
def create_project(request):
	c=None
	if request.method =='POST':
		name = request.POST.get('projectname','')
	#	print 'course name is : ',request.session['course']
		if 'course' in request.session:
			c = Course.objects.get(name=request.session['course'])
		if c :
			w = Project.objects.filter(course = c.id ,name = name )
			if len(w):
				return HttpResponse('project already exists')
			else:
				w = Project(name=name,course=c.id,pid=10)
				w.save()
				return HttpResponseRedirect(reverse('teacher_course',args=(c.name,)))
		else :
			return HttpResponse('course not exist')
						 


#####------delete api part-----------

#------delete course
@csrf_exempt
def delete_course(request):
	if request.method=='POST':
		cid = request.POST.get('course','')
#		print 'delete cid is: ',cid
		remove_course_by_id(cid)
		return HttpResponseRedirect(reverse('teacher_home'))
@csrf_exempt
def delete_test(request):
	if request.method == 'POST':
		tid = request.POST.get('test','')
		test = get_test_by_id(int(tid))
		cname = request.session['course']
		remove_test_by_id(tid)
		return HttpResponseRedirect(reverse('teacher_course',args=(cname,)))

@csrf_exempt
def delete_project(request):
	if request.method == 'POST':
		pid = request.POST.get('project','')
		remove_project_by_id(pid)
		cname = request.session['course']
		return HttpResponseRedirect(reverse('teacher_course',args=(cname,)))

####------------------remove api ---------------
##
##
##
#####-----------------------------------------

def remove_course_by_id(cid):
	if cid:
		course = Course.objects.filter(id=cid)
		for c in course:
			remove_test_by_course(c.id)
			remove_project_by_course(c.id)
			c.delete()
def remove_test_by_course(cid):
	if cid:
		test = Test.objects.filter(course=cid)
		if len(test):
			for t in test:
				remove_answer_by_test(t.id)
				t.delete()
			return True
	return
def remove_test_by_id(tid):
	if tid:
		test = Test.objects.get(id=tid)
		if test:
			remove_answer_by_test(test.id)
			test.delete()
			return True

def remove_project_by_id(pid):
	if pid:
		project = Project.objects.get(id=pid)
		if project:
			remove_question_by_project(project.id)
			project.delete()
			return True

def remove_project_by_course(cid):
	if cid:
		project = Project.objects.filter(course = cid)
		if project:
			for p in project:
				remove_question_by_project(p.id)
				p.delete()
			return True
				
def remove_question_by_project(pid):
	if pid:
		pq = Project_Question.objects.filter(pid = pid)
		for p in pq :
			pq.delete()
		return True
def remove_answer_by_test(tid):
	if tid:
		ta = Test_Answer.objects.filter(tid=tid)
		if len(ta):
			for t in ta:
				t.delete()
			return True
@csrf_exempt
def login_view(request):
   #logout(request)
   errors=[]
   errors.append("login out")
   return render_to_response('login.html',RequestContext(request,{'titel':'TeacherLogin','errors':errors}))


#####--------general api -----------

#----get course test --------
def get_test(coursename):
	course = Course.objects.filter(name=coursename)
	if len(course)==0:
		return None 
	elif len(course)==1:
		test = Test.objects.filter(course__exact=course[0].id)
		return test
	return None
#------get course project----
def get_project(coursename):
	course = Course.objects.filter(name=coursename)
	if len(course)==1:
		project = Project.objects.filter(course=course[0].id)
		return project
	return None
####--------upload test files-------
@csrf_exempt
def test_upload_file(request):
	if request.method=='POST':
		uf = UpForm(request.POST,request.FILES)
		if uf.is_valid():
			testfile = uf.cleaned_data['testfile']
			test = get_test_by_name(request.session['testname'],reqeust.session['courseid'])
			if test:
				test.files = testfile
				tid = test.id
				test._meta.get_field('files').upload_to='static/files/test/'+str(tid)+'/'
				test.files.name =str(tid)+'_'+ test.files.name
#				print 'url is: ',test.files.url,' ',test.files.name,' ',test.files.path,' ',test.files
				test.file_url='files/test/'+str(tid)+'/'+test.files.name
				test.save()
				return HttpResponseRedirect(reverse('course_test',args=(test.name,)))
			else:
				return HttpResponse('test is None')
		else :
			return HttpResponse('form is not valid')
	else:
		return HttpResponse('method is wrong')

class UpForm(forms.Form):
	testfile = forms.FileField()
#-----------------access database api ---
#
##
#
#
#---------------------------------------------
def get_test_by_name(testname,cid):
	if testname and cid:
		
		test = Test.objects.filter(name=testname,course=cid)
		if len(test):
			return test[0]
	return None

def get_test_by_id(tid):
	if tid:
#		print 'tid is : ',tid
		test = Test.objects.filter(id=tid)
		if len(test):
			return test[0]
	return None

def get_project_by_id(pid):
	if pid:
		p = Project.objects.filter(id=pid)
		if len(p):
			return p[0]
	return None
def get_project_by_name(pname,cid):
	if pname and cid:
		project = Project.objects.filter(name=pname,course=cid)
		if len(project):
			return project[0]
	return None
	

def get_course_by_id(cid):
	if cid:
		course = Course.objects.get(id=cid)
		if course:
			return course
		else:
			return None

def get_course_by_name(cname):
	if cname:
		c = Course.objects.filter(name = cname)
		if len(c):
			return c[0]
		return None
	return None

def get_project_by_course(cid):
	if cid:
		c = Course.objects.filter(id = cid)
		if len(c):
			plist = Project.objects.filter(course = cid)
			if len(plist):
				return plist
			return None
		return None
	return None

def get_question_by_project(pid):
	if pid:
		pq = Project_Question.objects.filter(pid = pid)
		if len(pq):
			return pq
		return None
	return None
def get_question_by_name(qname,pid):
	if qname and pid:
		question = Project_Question.objects.filter(name=qname,pid=pid)
		if len(question):
			return question[0]
		return None
	return None
#----------------------EOF
