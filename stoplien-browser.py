#!/usr/bin/env python3
import sys
from PyQt5.QtCore import QUrl
from PyQt5.QtWidgets import QApplication, QMainWindow, QToolBar, QAction, QLineEdit, QTabWidget
from PyQt5.QtWebEngineWidgets import QWebEngineView

HOME_HTML = """<!DOCTYPE html><html><head><meta charset="utf-8"><title>STOPlien Browser</title>
<style>
  body { background: #121216; color: #fff; font-family: sans-serif; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 90vh; margin: 0; }
  .logo { background: #dc1414; border: 4px solid #fff; padding: 18px 34px; font-size: 38px; font-weight: bold; border-radius: 14px; margin-bottom: 20px; box-shadow: 0 0 25px rgba(220,20,20,0.6); }
  h1 { margin: 0 0 25px 0; font-size: 26px; color: #f5c211; }
  form { display: flex; width: 90%; max-width: 560px; }
  input { flex: 1; padding: 14px 18px; font-size: 16px; border: 2px solid #dc1414; border-radius: 10px 0 0 10px; background: #1e1e24; color: #fff; outline: none; }
  button { padding: 14px 24px; font-size: 16px; font-weight: bold; background: #dc1414; color: #fff; border: none; border-radius: 0 10px 10px 0; cursor: pointer; }
</style></head><body>
  <div class="logo">STOP</div><h1>STOPlien Browser — Release 1.0</h1>
  <form action="https://duckduckgo.com" method="get">
    <input type="text" name="q" placeholder="Поиск в интернете или введите адрес..." autofocus>
    <button type="submit">Найти</button>
  </form></body></html>"""

class Browser(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("STOPlien Browser 1.0")
        self.resize(1200, 760)
        self.tabs = QTabWidget()
        self.tabs.setTabsClosable(True)
        self.tabs.tabCloseRequested.connect(self.close_tab)
        self.tabs.currentChanged.connect(self.update_url)
        self.setCentralWidget(self.tabs)
        nav = QToolBar()
        self.addToolBar(nav)
        for text, fn in [("◀", lambda: self.cur().back()), ("▶", lambda: self.cur().forward()), ("↻", lambda: self.cur().reload()), ("🏠", self.home), ("➕", lambda: self.add_tab())]:
            act = QAction(text, self)
            act.triggered.connect(fn)
            nav.addAction(act)
        self.url_bar = QLineEdit()
        self.url_bar.returnPressed.connect(self.nav_to)
        nav.addWidget(self.url_bar)
        self.add_tab()
    def cur(self): return self.tabs.currentWidget()
    def add_tab(self, url=None):
        b = QWebEngineView()
        if url: b.setUrl(QUrl(url))
        else: b.setHtml(HOME_HTML, QUrl("stoplien://home"))
        b.urlChanged.connect(lambda q, x=b: self.url_bar.setText(q.toString()) if x == self.cur() else None)
        b.titleChanged.connect(lambda t, x=b: self.tabs.setTabText(self.tabs.indexOf(x), t[:18]))
        self.tabs.setCurrentIndex(self.tabs.addTab(b, "Новая вкладка"))
    def close_tab(self, i):
        if self.tabs.count() > 1: self.tabs.removeTab(i)
    def home(self):
        self.cur().setHtml(HOME_HTML, QUrl("stoplien://home"))
        self.url_bar.setText("stoplien://home")
    def nav_to(self):
        t = self.url_bar.text().strip()
        if not t: return
        if "." in t and " " not in t:
            self.cur().setUrl(QUrl(t if t.startswith("http") else "https://" + t))
        else:
            self.cur().setUrl(QUrl("https://duckduckgo.com?q=" + t))
    def update_url(self, i):
        if self.cur(): self.url_bar.setText(self.cur().url().toString())

app = QApplication(sys.argv)
win = Browser()
win.show()
sys.exit(app.exec_())
