import requests


class BingSearch:
    def __init__(self, bing_api_key, bing_api_endpoint):
        self.bing_api_key = bing_api_key
        self.bing_api_endpoint = bing_api_endpoint
        self.query_result = None

    def search(self, query):
        headers = {"Ocp-Apim-Subscription-Key": self.bing_api_key}
        params = {"q": query, "mkt": "en-US"}

        try:
            response = requests.get(
                self.bing_api_endpoint, headers=headers, params=params
            )
            response.raise_for_status()
            self.query_result = response.json()
            return self.query_result
        except Exception as error:
            return error

    def get_first_link(self):
        return self.query_result["webPages"]["value"][0]["url"]
