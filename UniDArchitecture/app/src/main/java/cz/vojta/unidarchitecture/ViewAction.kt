package cz.vojta.unidarchitecture
enum class Kind {
    ENTER, REFRESH
}
abstract class ViewAction(kind: Kind)

class Params : ViewAction(Kind.ENTER)
class Enter : ViewAction(Kind.ENTER)

