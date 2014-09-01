import getopt
import sys
from flask import Flask, request, render_template
from cherrypy import wsgiserver
from functools import wraps
from models2 import User, Account
from database import db_session
import simplejson as json
app = Flask(__name__)
DEFAULT_PORT = 8888
makejson = json.dumps


def error(text):
    return makejson({"error" : text})

def success(text):
    return makejson({"success" : text})


@app.route('/login', methods=['POST'])
def login():
    Responsemsg="fail"
    # for below line to work, content type should be sent along with the request. request.form,request.json, request.data, request.values can be used.
    # for json: {"uuid":"admin","password":"admin"}  #for data: username=dns
    user = request.form['username']
    #print user
    u = User.query.filter(User.username == request.form["username"]).first() #checks for presence of user in the database #requires models.py
    #print "u=",u
    if not u or u.password != request.form["password"]:
        Responsemsg="Wrong Credentials"
    else: Responsemsg="Correct Credentials"
    data = {"message" : Responsemsg, "user": user}
    print makejson(data)
    return makejson(data)


@app.route('/transfer', methods=['POST'])
def transfer():
    Responsemsg="fail"
    # for below line to work, content type should be sent along with the request. request.form,request.json, request.data, request.values can be used.
    # for json: {"uuid":"admin","password":"admin"}  #for data: username=dns
    user = request.form['username']
    #print user
    u = User.query.filter(User.username == request.form["username"]).first() #checks for presence of user in the database #requires models.py
    #print "u=",u
    if not u or u.password != request.form["password"]:
        Responsemsg="Wrong Credentials so trx fail"
    else: 
	Responsemsg="Correct Credentials so trx continue"
    data = {"message" : Responsemsg, "user": user}
    print makejson(data)
    return makejson(data)

@app.route('/getaccounts', methods=['POST'])
def getaccounts():
    Responsemsg="fail"
    acc1=0
    acc2=0
    # for below line to work, content type should be sent along with the request. request.form,request.json, request.data, request.values can be used.
    # for json: {"uuid":"admin","password":"admin"}  #for data: username=dns
    print request
    user2 = request.form['username']
    print user2;
    #print user
    u2 = User.query.filter(User.username == user2).first() #checks for presence of user in the database #requires models.py
    #print "u=",u
    from_account = Account.query.filter(Account.account_number == "123456789").first()
    if not u2 or u2.password != request.form["password"]:
        Responsemsg="Wrong Credentials so trx fail"
    else: 
	Responsemsg="Correct Credentials so get accounts will continue"
        acc1=1234656789
	acc2=987654321
    data = {"message" : Responsemsg, "acc1": acc1,"acc2": acc2}
    print makejson(data)
    return makejson(data)


def usage():
    print "Paladion Insecure Bank Application"
    print "Options: "
    print "  --portno p     Webserver runs on portno p with default as 8888"
    print "  --help      	Usage Details"


if __name__ == '__main__':
    port_no = DEFAULT_PORT
    ssl = False
    opts, args = getopt.getopt(sys.argv[1:], "", ["help", "port_no="])
    for o, a in opts:
        if o == "--help":
            usage()
            sys.exit(2)
        
        
        elif o == "--port_no":
            port_no = int(a)
# The following snippet let you run the Flask app on top of the WSGI server shipped with CherryPy at specified port
    d = wsgiserver.WSGIPathInfoDispatcher({'/': app})
    server = wsgiserver.CherryPyWSGIServer(('127.0.0.1', 8888), d, timeout=300)

    print "WebServer running on port %d %s" % (port_no, "(debug enabled)" if app.debug else "")
    
    try:
        server.start()
    except KeyboardInterrupt:
        server.stop()
