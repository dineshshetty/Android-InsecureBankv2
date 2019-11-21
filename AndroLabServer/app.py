import getopt
import web
import sys
#from web.wsgiserver import CherryPyWSGIServer
#from cherrypy import wsgiserver
from cheroot import wsgi # This replaces the 2 above
from flask import Flask, request, request_started
from functools import wraps
from models import User, Account
from database import db_session
import simplejson as json
makejson = json.dumps
app = Flask(__name__)
makejson = json.dumps

DEFAULT_PORT_NO = 8888

def usageguide():
    print "InsecureBankv2 Backend-Server"
    print "Options: "
    print "  --port p    serve on port p (default 8888)"
    print "  --help      print this message"

@app.errorhandler(500)
def internal_servererror(error):
    print " [!]", error
    return "Internal Server Error", 500

'''
The function handles the authentication mechanism
'''
@app.route('/login', methods=['POST'])
def login():
    Responsemsg="fail"
    user = request.form['username']
    #checks for presence of user in the database #requires models.py
    u = User.query.filter(User.username == request.form["username"]).first()
    print "u=",u
    if u and u.password == request.form["password"]:
	Responsemsg="Correct Credentials"
    elif u and u.password != request.form["password"]:
	Responsemsg="Wrong Password"
    elif not u:
        Responsemsg="User Does not Exist"
    else: Responsemsg="Some Error"
    data = {"message" : Responsemsg, "user": user}
    print makejson(data)
    return makejson(data)

'''
The function responds back with the from and to debit accounts corresponding to logged in user
'''
@app.route('/getaccounts', methods=['POST'])
def getaccounts():
    #set accounts from the request 
    Responsemsg="fail"
    acc1=acc2=from_acc=to_acc=0
    user=request.form['username']
    #checks for presence of user in the database
    u = User.query.filter(User.username == user).first()
    if not u or u.password != request.form["password"]:
        Responsemsg="Wrong Credentials so trx fail"
    else:
        Responsemsg="Correct Credentials so get accounts will continue"
        a=Account.query.filter(Account.user == user)
        for i in a:
          if (i.type=='from'):
	    from_acc=i.account_number;
        for j in a:
          if (i.type=='to'):
	    to_acc=i.account_number;
    data = {"message" : Responsemsg, "from": from_acc,"to": to_acc}
    print makejson(data)
    return makejson(data)

'''
The function takes a new password as input and passes it on to the change password module
'''
@app.route('/changepassword', methods=['POST'])
def changepassword():
    #set accounts from the request 
    Responsemsg="fail"
    newpassword=request.form['newpassword']
    user=request.form['username']
    print newpassword
    u = User.query.filter(User.username == user).first() #checks for presence of user in the database
    if not u:
        Responsemsg="Error"
    else:
	Responsemsg="Change Password Successful"
	u.password = newpassword
        db_session.commit()
    data = {"message" : Responsemsg}
    print makejson(data)
    return makejson(data)
    
'''
The function handles the transaction module
'''
@app.route('/dotransfer', methods=['POST'])
def dotransfer():
    #set accounts from the request 
    Responsemsg="fail"
    user=request.form['username']
    amount=request.form['amount']
    #print request.form["from_acc"]
    u = User.query.filter(User.username == user).first() #checks for presence of user in the database
    if not u or u.password != request.form["password"]:
        Responsemsg="Wrong Credentials so trx fail"
	#print Responsemsg
    else:
	Responsemsg="Success"
	#print Responsemsg
	from_acc = request.form["from_acc"]
	to_acc = request.form["to_acc"]
	amount = request.form["amount"]
        from_account = Account.query.filter(Account.account_number == from_acc).first()
        to_account = Account.query.filter(Account.account_number == to_acc).first()
	#print "fromacc=",from_account
        #print "amount===",amount
        to_account.balance += int(request.form['amount'])
        from_account.balance -= int(request.form['amount'])
        db_session.commit()
    data = {"message" : Responsemsg, "from": from_acc, "to": to_acc,  "amount": amount}
    #print makejson(data)
    return makejson(data)

'''
The function provides login mechanism to a developer user during development phase
'''
@app.route('/devlogin', methods=['POST'])
def devlogin():
    user=request.form['username']
    Responsemsg="Correct Credentials"
    data = {"message" : Responsemsg, "user": user}
    print makejson(data)
    return makejson(data)

if __name__ == '__main__':
    port = DEFAULT_PORT_NO
    options, args = getopt.getopt(sys.argv[1:], "", ["help", "port="])
    for op, arg1 in options:
	if op == "--help":
            usageguide()
            sys.exit(2)
        elif op == "--port":
            port = int(arg1)

    urls = ("/.*", "app")
    apps = web.application(urls, globals())
    server = wsgi.Server(("0.0.0.0", port),app,server_name='localhost')
    print "The server is hosted on port:",(port)
    
    try:
        server.start()
	#apps.run(port)
    except KeyboardInterrupt:
        server.stop()
