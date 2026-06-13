import type { ResourceGroup } from '../../api/modules/workCenter';

export const mockResourceGroups: ResourceGroup[] = [
  {
    id: 1,
    workCenter: 'WC01',
    resourceGroupCode: 'WC01-RSC014',
    resourceGroupName: '滚筒切纸机',
    forecastResourceGroupCode: 'WC01-VRSC001',
    forecastResourceGroupName: '预测滚筒切纸',
    resources: [
      {
        id: 1,
        resourceCode: 'WC01-RSC014-P06-1121-00002',
        name: '1121-00002-滚筒切纸机-厚纸板-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      },
      {
        id: 2,
        resourceCode: 'WC01-RSC014-P06-1121-00003',
        name: '1121-00003-滚筒切纸机-薄纸板-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  },
  {
    id: 2,
    workCenter: 'WC01',
    resourceGroupCode: 'WC01-RSC015',
    resourceGroupName: '滚筒分条机',
    forecastResourceGroupCode: 'WC01-VRSC001',
    forecastResourceGroupName: '滚筒分条',
    resources: [
      {
        id: 3,
        resourceCode: 'WC01-RSC015-P06-1036-00009',
        name: '1036-00009-滚筒分条机-分条/回卷-9B地台',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  },
  {
    id: 3,
    workCenter: 'WC02A',
    resourceGroupCode: 'WC02A-PIT100',
    resourceGroupName: '2X2C双色小森-六期',
    forecastResourceGroupCode: 'WC02A-VPIT001',
    forecastResourceGroupName: '预测非水油印刷',
    resources: [
      {
        id: 4,
        resourceCode: 'WC02A-PIT100-P06-1234-00001',
        name: '1234-00001-2X2C双色小森-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      },
      {
        id: 5,
        resourceCode: 'WC02A-PIT100-P06-1234-00002',
        name: '1234-00002-2X2C双色小森-六期2楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  },
  {
    id: 4,
    workCenter: 'WC02A',
    resourceGroupCode: 'WC02A-PIT101',
    resourceGroupName: '普通八色小森-六期',
    forecastResourceGroupCode: 'WC02A-VPIT001',
    forecastResourceGroupName: '预测非水油印刷',
    resources: [
      {
        id: 6,
        resourceCode: 'WC02A-PIT101-P06-1235-00001',
        name: '1235-00001-普通八色小森-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  },
  {
    id: 5,
    workCenter: 'WC02A',
    resourceGroupCode: 'WC02A-PIT102',
    resourceGroupName: '双色海德堡-六期',
    forecastResourceGroupCode: 'WC02A-VPIT001',
    forecastResourceGroupName: '预测非水油印刷',
    resources: [
      {
        id: 7,
        resourceCode: 'WC02A-PIT102-P06-1236-00001',
        name: '1236-00001-双色海德堡-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  },
  {
    id: 6,
    workCenter: 'WC02B',
    resourceGroupCode: 'WC02B-PIT200',
    resourceGroupName: '预测水油印刷',
    forecastResourceGroupCode: 'WC02B-VPIT002',
    forecastResourceGroupName: '预测水油印刷',
    resources: [
      {
        id: 8,
        resourceCode: 'WC02B-PIT200-P06-1237-00001',
        name: '1237-00001-水油印刷机-六期1楼',
        resourceType: 'M',
        asset: 'M',
        sortingRule: '全局排序规则',
        selectionRule: '排序规则',
        layeringRule1: '排序规则',
        layeringRule2: '排序规则',
        layeringRule3: '排序规则',
        selectionValue: '0N',
        mustComplete: 0,
        instantaneousCount: 0,
        extensionDelay: 0
      }
    ]
  }
];
























