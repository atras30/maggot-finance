## API Documentation
### Available Endpoints

#### Authentications
##### post /auth/login

required body : ["email", "password"]\
returns : \
1. 'message' : 'Bad credentials.' if username / password is wrong (200)\
2. ['token', 'user'] if the given data is correct (200)\
\
post /auth/logout\
must include Authorization with bearer token for the corresponding user.\
returns :\
1. 'message' : 'Successfully logged out.' (200)

post /auth/register\
rqeuired body : []... belum
\
get /user\
post /user\
put /user/{id}\
get /user/role/{role}\
get /user/username/{username}\
\
get /transaction\
\
post /peternak/sell/maggots\