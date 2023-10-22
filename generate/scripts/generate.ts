import {mkdirSync, readdirSync, readFileSync, writeFileSync} from 'fs';

function generateIcons() {
  mkdirSync('src/main/resources/icons', {recursive: true});
  readdirSync('generate/vscode-icons/src/icons').forEach((file) => {
    if (!file.endsWith('.svg')) {
      return
    }

    let data = readFileSync(`generate/vscode-icons/src/icons/${file}`, {encoding: 'utf-8'});
    data = data.replace("width=\"100.0px\"", "width=\"16.0px\"");
    data = data.replace("height=\"100.0px\"", "height=\"16.0px\"");
    writeFileSync(`src/main/resources/icons/${file}`, data, {encoding: 'utf-8'})
  });
}

generateIcons();
