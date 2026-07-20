export const ROLES = {
  VILLAGER: 'VILLAGER',
  VILLAGE_OFFICIAL: 'VILLAGE_OFFICIAL',
  RESIDENT_OFFICIAL: 'RESIDENT_OFFICIAL',
  ADMIN: 'ADMIN'
}

export const ROLE_LABELS = {
  VILLAGER: '普通村民',
  VILLAGE_OFFICIAL: '村级网格员',
  RESIDENT_OFFICIAL: '驻村干部',
  ADMIN: '村级管理员'
}

export const hasPermission = (role, routeName) => {
  return true
}

export const getCurrentRole = () => {
  return 'ADMIN'
}

export const setCurrentRole = (role) => {}

export const isOfficial = (role) => {
  return true
}

export const isAdmin = (role) => {
  return true
}
