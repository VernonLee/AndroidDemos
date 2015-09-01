# CheckableLayoutDemo

###问题
-------------------------
GridView和ListView都可以设置选择模式(Choice Mode)，但是遇到较复杂的子View，
比如既包含文字处理又有图片，由于涉及到的东西比较多，所以处理起来较为繁琐。
如果将这些处理封装起来，那么对开发将有很大的帮助。

###处理
------------------------
CheckableView是对这些处理封装，实现Checkable接口，包含setChecked(),isChecked()和toggle()方法
。isChecked()和toggle()中的处理少而且简单，UI选中状态效果的处理在setChecked()方法中进行。

###操作
-------------------------
因为GridView和ListView的选择模式和Checable接口有想通的地方(猜测), 只要设置好
对应的选择模式后，不需要做其他任何处理，就可以实现想要的选择效果。
