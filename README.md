# Risk

Risk analysis microsservice. The analysis is made via integration with the [ClearSale API](https://api.clearsale.com.br/docs/total-totalGarantido-application).

## Features
Risk analysis:
After receiving the request, it's converted and sent to ClearSale.

Status change notification:
Clear sale calls this endpoint via webhook to notify the status change of an analysis. The microsservice publish it to an internal Queue and proccess the result asynchronously.

Status proccessing:
Consumes the internal queue and makes a request to ClearSale to get the real status of the analysis. If the request is not successfull, the proccess will keep retrying a determined ammount of times untill it succeeds or the tentatives limit is reached. If the ammount of tentatives is reached, the status change notification is published to a DLQ.

Analysis result notification:
After processing the result, a notification is published to an Exchange so all the interested subscribers can be notified.
