import getopt
import sys
from flask import Flask, request, render_template, request_started
from cherrypy import wsgiserver
from functools import wraps
from models import User, Account
from database import db_session
import simplejson as json
makejson = json.dumps
import datetime
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

@app.route('/login', methods=['POST'])
def login():
    Responsemsg="fail"
    # for below line to work, content type should be sent along with the request. request.form,request.json, request.data, request.values can be used.
    # for json: {"uuid":"admin","password":"admin"}  #for data: username=dns
    user = request.form['username']
    #print user
    u = User.query.filter(User.username == request.form["username"]).first() #checks for presence of user in the database #requires models.py
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

@app.route('/getaccounts', methods=['POST'])
def getaccounts():
    #set accounts from the request 
    Responsemsg="fail"
    acc1=acc2=from_acc=to_acc=0
    user=request.form['username']
    print user
    u = User.query.filter(User.username == user).first() #checks for presence of user in the database
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

    dispatch = wsgiserver.WSGIPathInfoDispatcher({'/': app})
    server = wsgiserver.CherryPyWSGIServer(('0.0.0.0', port), dispatch, timeout=200)

    print "The server is hosted on port:",(port)
    
    try:
        server.start()
    except KeyboardInterrupt:
        server.stop()

