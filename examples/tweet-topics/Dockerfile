# start by pulling the python image
FROM python:3.8

# copy the requirements file into the image
COPY ./requirements.txt /app/requirements.txt

# install the dependencies and packages in the requirements file
RUN pip install --no-cache-dir -r /app/requirements.txt --extra-index-url https://download.pytorch.org/whl/cpu

RUN python -c "import tweetnlp; tweetnlp.load_model('topic_classification',multi_label=False)"

# switch working directory
WORKDIR /app


# copy every content from the local file to the image
COPY . /app

EXPOSE 5000

# configure the container to run in an executed manner
ENTRYPOINT [ "python" ]

CMD ["app.py" ]