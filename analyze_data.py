import json
import sys
sys.stdout.reconfigure(encoding='utf-8')

data = json.load(open(r'c:\Users\UserX\.trae-cn\attachments\6a843424826437c145e3a3ef\extracted_data.json', 'r', encoding='utf-8'))

print('=== 数据源总览 ===')
for k, v in data.items():
    if isinstance(v, list):
        print(f'{k}: {len(v)} 条记录')
    elif isinstance(v, dict):
        print(f'{k}: {len(v)} 个子表 -> {list(v.keys())}')
        for sk, sv in v.items():
            if isinstance(sv, list):
                print(f'  {sk}: {len(sv)} 行')

# 切坡建房统计
q = data.get('重点部位', {}).get('切坡建房', [])
print(f'\n切坡建房: {len(q)} 行 (含表头)')

# 供水工程
print(f'\n供水工程:')
for item in data.get('供水工程', []):
    name = item.get('行政村镇', '') or item.get('工程名称', '') or str(item)[:60]
    print(f'  {name}')

# 重点敲门
kd = data.get('重点敲门', {})
print(f'\n重点敲门: total_rows={kd.get("total_rows")}, 有效记录={len(kd.get("data", []))}')

# 燃气
print(f'\n燃气: {len(data.get("燃气", []))} 条')

# 减灾能力汇总
print(f'\n减灾能力各村基本情况:')
for item in data.get('减灾能力', []):
    name = item.get('社区（行政村）名称', '')
    pop = item.get('常住人口数量', '')
    area = item.get('本级灾害应急避难场所容量', '')
    funds = item.get('上一年度防灾减灾救灾资金投入总金额', '')
    print(f'  {name}: 人口={pop}, 避难容量={area}, 资金={funds}万元')

# 重点部位汇总
huizong = data.get('重点部位', {}).get('汇总', [])
print(f'\n重点部位汇总表({len(huizong)}行):')
for row in huizong:
    vals = [v for v in row if v is not None]
    if vals:
        print(f'  {vals}')

# 水库
shuiku = data.get('重点部位', {}).get('水库', [])
print(f'\n水库统计({len(shuiku)}行):')
for row in shuiku:
    vals = [v for v in row if v is not None]
    if vals and isinstance(vals[0], int):
        print(f'  {vals}')

# 燃气详情
ranqi = data.get('燃气', [])
print(f'\n燃气排查台账({len(ranqi)}条):')
for row in ranqi:
    vals = [v for v in row if v is not None]
    if vals and len(vals) > 3:
        print(f'  {vals}')
