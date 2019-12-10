# Example of libraries

- [imagehelper](../../../tree/master/imagehelper/) : ImagePicker / ImageViewer
- [permissionmanager](../../../tree/master/permissionmanager/) : Permission check
- [adhelper](../../../tree/master/adhelper/) : Google Admob, Facebook AudienceNetwork


# Sample of architecture

Get my profile information from github api (graphQL). You should Github API key to `/apikey.properties` file


<br><br>

## My Android Architecture

- Kotlin **Coroutines** for background operations.
- A single-activity architecture, using the **Navigation component** to manage fragment operations.
- A presentation layer that contains a fragment (View) and a ViewModel per screen (or feature).
- **Reactive UIs** using LiveData observables.
- Koin for **DI**
- Apollo-client for **GraphQL** api request.

