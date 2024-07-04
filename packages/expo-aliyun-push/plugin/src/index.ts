import {ConfigPlugin} from 'expo/config-plugins';

import type {AliyunPushConfig} from './shared';
import withAliyunConfigAndroid from './withAndroidConfig';
import withAliyunConfigIOS from './withIOSConfig';

export type * from './shared';

const withAliyunConfig: ConfigPlugin<AliyunPushConfig> = (
  config,
  pluginConfig,
) => {
  return [withAliyunConfigAndroid, withAliyunConfigIOS].reduce(
    (c, p) => p(c, pluginConfig),
    config,
  );
};

export default withAliyunConfig;
