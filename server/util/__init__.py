import pathlib
def replace(fileName:str|pathlib.Path, old:str, new:str):
    file = fileName if type(fileName)==pathlib.Path else pathlib.Path(fileName)
    if not file.exists():
        raise FileNotFoundError(f"File {file} does not exist.")
    with file.open('r') as f:
        content = f.read()
    content  = content.replace(old, new)
    with file.open('w') as f:
        f.write(content)
def getRootDir() -> pathlib.Path:
    rootDir= pathlib.Path(__file__).parent.parent.parent
    if not rootDir.exists():
        raise FileNotFoundError(f"Root directory {rootDir} does not exist.")
    return rootDir

def getResourceDir() -> pathlib.Path:
    resourceDir= getRootDir()/"src"/"main"/"resources"
    if not resourceDir.exists():
        raise FileNotFoundError(f"Resource directory {resourceDir} does not exist.")
    return resourceDir