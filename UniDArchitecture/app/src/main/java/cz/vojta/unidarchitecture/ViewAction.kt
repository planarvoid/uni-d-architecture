package cz.vojta.unidarchitecture
sealed class ViewAction

class Params : ViewAction()
class Refresh : ViewAction()
class LoadNext: ViewAction()

