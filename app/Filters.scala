import javax.inject.Inject

import filters.LoggingFilter
import play.api.http.{DefaultHttpFilters, EnabledFilters}

class Filters @Inject() (
                          defaultFilters: EnabledFilters,
                          log: LoggingFilter
                        ) extends DefaultHttpFilters(defaultFilters.filters :+ log: _*)