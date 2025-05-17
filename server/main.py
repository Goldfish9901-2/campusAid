from util import replace,getRootDir,getResourceDir
if __name__ == '__main__':
    print(getRootDir())
    replace(getResourceDir()/"alipay.properties","http://m6626a.natappfree.cc","http://misaka52065.w1.luyouxia.net")
    replace(getResourceDir()/"application.yml","8090","8000")