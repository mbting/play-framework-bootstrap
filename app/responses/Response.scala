package responses

object Response {
  def success(code: Int, message: String, payload: Any): Response = Response(success = true, code, message, Some(payload))
  def success(payload: Any): Response = Response(success = true, 1, "Success", Some(payload))
  def success(): Response = Response(success = true, 1, "Success", None)
  def failure(code: Int, message: String, payload: Any): Response = Response(success = false, code, message, Some(payload))
  def failure(payload: Any): Response = Response(success = false, 0, "Failure", Some(payload))
  def failure(): Response = Response(success = false, 0, "Failure", None)
}

case class Response(success: Boolean, code: Int, message: String, payload: Option[Any] = None)
