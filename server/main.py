from util import replace, getResourceDir
from pathlib import Path
if __name__ == '__main__':
    resource_dir = getResourceDir()

    # 替换支付宝回调地址中的域名
    replace(resource_dir / "alipay.properties", "http://m6626a.natappfree.cc", "http://misaka52065.w1.luyouxia.net")

    # 直接替换 localhost:${server.port} → misaka52065.w1.luyouxia.net
    replace(resource_dir / "alipay.properties", "localhost:${server.port}", "misaka52065.w1.luyouxia.net")

    # 替换 application.yml 中的端口（从 8090 → 8000）
    replace(resource_dir / "application.yml", "8090", "8000")
    
    for path in (resource_dir/"static").rglob("*.html"):
        # 替换图像为镜像源
        replace(path,"/background2.jpg","http://101.37.75.201:8000/background2.jpg")
    