# coding: utf-8

from locust import HttpLocust, TaskSet, task
import random, json

# 红包id
ENVELOPE_ID = 29

class GrabRedEnvelope(TaskSet):
    def on_stop(self):
        """ on_stop is called when the TaskSet is stopping """
        # 验证结果 (对账)
        with self.client.get("/red-envelope/verify?envelopeIds={}".format(ENVELOPE_ID), catch_response=True) as response:
            if response.status_code == 200:
                content = json.loads(response.content)
                if content["status"] == True:
                    response.success()
                else:
                    response.failure("response content status is False, content: {}".format(response.content))
            else:
                response.failure("status_code is not 200, content: {}".format(response.content))

    @task
    def grab(self):
        grabber = "grabber" + str(random.randint(0, 1000))
        self.client.get("/red-envelope/grab?envelopeId={}&grabber={}".format(ENVELOPE_ID, grabber))

class RedEnvelopeWebsite(HttpLocust):
    task_set = GrabRedEnvelope
    min_wait = 0
    max_wait = 9000