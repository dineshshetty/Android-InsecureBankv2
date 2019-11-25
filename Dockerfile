FROM frolvlad/alpine-python2

COPY AndroLabServer /

RUN pip2 install flask
RUN pip2 install flask-sqlalchemy
RUN pip2 install simplejson
RUN pip2 install cherrypy
RUN pip2 install web.py

CMD [ "python", "app.py" ]
