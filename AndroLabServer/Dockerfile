FROM python:2.7

RUN useradd -m adrserver
WORKDIR /app
COPY src/ .
RUN apt update -y
RUN pip install --upgrade --no-cache-dir -r requirements.txt
RUN chown -R root:adrserver /app && \
    chmod 750 /app /app/app.py
USER adrserver
CMD ["/usr/local/bin/python", "/app/app.py"]
EXPOSE 8888