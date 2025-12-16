import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  getConnectionStatus(): Promise<object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('SecureNetwork');
