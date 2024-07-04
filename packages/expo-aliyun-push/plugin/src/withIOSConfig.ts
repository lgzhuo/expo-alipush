import {mergeContents} from '@expo/config-plugins/build/utils/generateCode';
import {
  withDangerousMod,
  withInfoPlist,
  type ConfigPlugin,
} from 'expo/config-plugins';
import fs from 'node:fs';
import path from 'node:path';

import type {AliyunIOSConfig, AliyunPushConfig} from './shared';

const ALIYUN_POD_SOURCE = `source 'https://github.com/aliyun/aliyun-specs.git'`;

const withAliyunConfigIOS: ConfigPlugin<AliyunPushConfig> = (config, {ios}) => {
  config = withAliyunRepo(config);
  if (ios) {
    config = withAliyunAppConfig(config, ios);
  }
  return config;
};

const withAliyunRepo: ConfigPlugin<void> = config => {
  return withDangerousMod(config, [
    'ios',
    async config => {
      const podFilePath = path.join(
        config.modRequest.platformProjectRoot,
        'Podfile',
      );
      const contents = await fs.promises.readFile(podFilePath, 'utf-8');
      const result = mergeContents({
        src: contents,
        newSrc: ALIYUN_POD_SOURCE,
        tag: 'add aliyun repo source',
        anchor: /^/,
        offset: 0,
        comment: '#',
      });
      if (result.didMerge) {
        await fs.promises.writeFile(podFilePath, result.contents, 'utf-8');
      }

      return config;
    },
  ]);
};

const withAliyunAppConfig: ConfigPlugin<AliyunIOSConfig> = (
  config,
  {appKey, appSecret},
) => {
  withInfoPlist(config, config => {
    config.modResults['ALIYUN_EMAS_APP_KEY'] = appKey;
    config.modResults['ALIYUN_EMAS_APP_SECRET'] = appSecret;
    return config;
  });
  return config;
};

export default withAliyunConfigIOS;
