[![CircleCI](https://circleci.com/gh/mcsheehan/github-search.svg?style=svg)](https://circleci.com/gh/mcsheehan/github-search)

An android application demonstrating usage of the github rest api using retrofit. I have used gitflow and feature branches in the construction of the application.

The most popular repositories are shown first and the star rating of the repository is shown next to it. It is possible to filter the results by programming language.

Searching occurs whilst typing, however github rest api is severely rate limited. A de-bounce of 500ms was added using RxJava however it is still quite easy to hit the rate limit. Searching as typing has been left in as a demonstration of the feature, though the latency of the calls and the rate limit make this feature somewhat less of a good user experience.

RxJava / RxKotlin has been used to connect retrofit to the LiveData components, which are observed from the view-model by the view.

The application uses MVVM to display the information from the github rest api.

Some unit tests have been included for the github rest api, these are not extensive.

A navigation graph has been included allowing for extra views to be added and navigated between easily.

Continuous integration has been set-up using circle-ci to ensure that the code builds in the cloud (no broken master branches).

An example of a github rest api call : https://api.github.com/search/repositories?q=tetris+language:python+language:assembly&sort=stars

Infinite scroll has not been conducted as the api only receives 30 results per page / query with severe rate limiting.
