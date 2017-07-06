FROM python:2.7
WORKDIR /home/data
COPY AndroLabServer/requirements.txt /home/data/requirements.txt
RUN pip install -r requirements.txt
COPY AndroLabServer/* /home/data/
CMD python app.py
