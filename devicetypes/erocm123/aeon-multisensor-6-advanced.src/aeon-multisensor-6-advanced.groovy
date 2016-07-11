/**
 *
 *  Aeon Multisensor 6 (Advanced)
 *   
 *	github: Eric Maycock (erocm123)
 *	email: erocmail@gmail.com
 *	Date: 2016-07-07
 *	Copyright Eric Maycock
 *
 *  Code has elements from other community sources @CyrilPeponnet, @Robert_Vandervoort. Greatly reworked and 
 *  optimized for improved battery life (hopefully) :) and ease of advanced configuration. I tried to get it
 *  as feature rich as possible. 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

 metadata {
	definition (name: "Aeon Multisensor 6 (Advanced)", namespace: "erocm123", author: "Eric Maycock") {
		capability "Motion Sensor"
		capability "Acceleration Sensor"
		capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Illuminance Measurement"
		capability "Ultraviolet Index" 
		capability "Configuration"
		capability "Sensor"
		capability "Battery"
        capability "Refresh"
        capability "Tamper Alert"
        
        command "resetBatteryRuntime"
		
        attribute   "needUpdate", "string"
        
        fingerprint deviceId: "0x2101", inClusters: "0x5E,0x86,0x72,0x59,0x85,0x73,0x71,0x84,0x80,0x30,0x31,0x70,0xEF,0x5A,0x98,0x7A"
        fingerprint deviceId: "0x2101", inClusters: "0x5E,0x86,0x72,0x59,0x85,0x73,0x71,0x84,0x80,0x30,0x31,0x70,0x7A,0x5A,0xEF" 
        fingerprint mfr: "0134", prod: "0258", model: "0100"
	}
    preferences {
        
        input description: "Once you change values on this page, the \"Synced\" Status will become \"Pending\" status. You can then force the sync by triple clicking the device button or just wait for the next WakeUp (60 minutes).", displayDuringSetup: false, type: "paragraph", element: "paragraph"
        
		generate_preferences(configuration_model())
        
    }
	simulator {
	}
	tiles (scale: 2) {
		multiAttributeTile(name:"main", type:"generic", width:6, height:4) {
			tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
            	attributeState "temperature",label:'${currentValue}°', icon:"st.motion.motion.inactive", backgroundColors:[
                	[value: 32, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 92, color: "#d04e00"],
					[value: 98, color: "#bc2323"]
				]
            }
            tileAttribute ("statusText", key: "SECONDARY_CONTROL") {
				attributeState "statusText", label:'${currentValue}'
			}
		}
        standardTile("motion","device.motion", width: 2, height: 2) {
            	state "active",label:'motion',icon:"st.motion.motion.active",backgroundColor:"#53a7c0"
                state "inactive",label:'no motion',icon:"st.motion.motion.inactive",backgroundColor:"#ffffff"
		}
		valueTile("temperature","device.temperature", width: 2, height: 2) {
            	state "temperature",label:'${currentValue}°',backgroundColors:[
                	[value: 32, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
					[value: 74, color: "#44b621"],
					[value: 84, color: "#f1d801"],
					[value: 92, color: "#d04e00"],
					[value: 98, color: "#bc2323"]
				]
		}
		valueTile("humidity","device.humidity", width: 2, height: 2) {
           	state "humidity",label:'RH ${currentValue} %',unit:""
		}
		valueTile(
        	"illuminance","device.illuminance", width: 2, height: 2) {
            	state "luminosity",label:'${currentValue} lux', unit:"lux", backgroundColors:[
                	[value: 0, color: "#000000"],
                    [value: 1, color: "#060053"],
                    [value: 3, color: "#3E3900"],
                    [value: 12, color: "#8E8400"],
					[value: 24, color: "#C5C08B"],
					[value: 36, color: "#DAD7B6"],
					[value: 128, color: "#F3F2E9"],
                    [value: 1000, color: "#FFFFFF"]
				]
		}
		valueTile(
        	"ultravioletIndex","device.ultravioletIndex", width: 2, height: 2) {
				state "ultravioletIndex",label:'${currentValue} UV INDEX',unit:""
		}
		standardTile("acceleration", "device.acceleration", width: 2, height: 2) {
			state("active", label:'tamper', icon:"st.motion.acceleration.active", backgroundColor:"#f39c12")
			state("inactive", label:'clear', icon:"st.motion.acceleration.inactive", backgroundColor:"#ffffff")
		}
        valueTile("tamper", "device.tamper", decoration: "flat", width: 2, height: 2) {
			state("detected", label:'tamper active', backgroundColor:"#f39c12")
			state("clear", label:'tamper clear', backgroundColor:"#53a7c0")
		}
		valueTile("battery", "device.battery", decoration: "flat", width: 2, height: 2) {
			state "battery", label:'${currentValue}% battery', unit:""
		}
        valueTile(
			"currentFirmware", "device.currentFirmware", decoration: "flat", width: 2, height: 2) {
			state "currentFirmware", label:'Firmware: v${currentValue}', unit:""
		}
        standardTile("refresh", "device.switch", decoration: "flat", width: 2, height: 2) {
			state "default", label:'Refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
		}
        valueTile("configure", "device.needUpdate", decoration: "flat", width: 2, height: 2) {
            state("NO" , label:'Synced', action:"configuration.configure", backgroundColor:"#8acb47")
            state("YES", label:'Pending', action:"configuration.configure", backgroundColor:"#f39c12")
        }
        valueTile(
			"batteryRuntime", "device.batteryRuntime", decoration: "flat", width: 2, height: 2) {
			state "batteryRuntime", label:'Battery: ${currentValue} Double tap to reset counter', unit:"", action:"resetBatteryRuntime"
		}
        valueTile(
			"statusText2", "device.statusText2", decoration: "flat", width: 2, height: 2) {
			state "statusText2", label:'${currentValue}', unit:"", action:"resetBatteryRuntime"
		}
        
		main([
        	"main", "motion"
            ])
		details([
        	"main",
            "humidity","illuminance","ultravioletIndex",
            "motion","tamper","battery", 
            "refresh", "configure", "statusText2", 
            ])
	}
}

def parse(String description)
{
	def result = []
    switch(description){
        case ~/Err 106.*/:
			state.sec = 0
			result = createEvent( name: "secureInclusion", value: "failed", isStateChange: true,
			descriptionText: "This sensor failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.")
        break
		case "updated":
        	log.debug "Update is hit when the device is paired."
            result << response(zwave.wakeUpV1.wakeUpIntervalSet(seconds: 3600, nodeid:zwaveHubNodeId).format())
            result << response(zwave.batteryV1.batteryGet().format())
            result << response(zwave.versionV1.versionGet().format())
            result << response(zwave.manufacturerSpecificV2.manufacturerSpecificGet().format())
            result << response(zwave.firmwareUpdateMdV2.firmwareMdGet().format())
            result << response(configure())
        break
        default:
			def cmd = zwave.parse(description, [0x31: 5, 0x30: 2, 0x84: 1])
			if (cmd) {
				result += zwaveEvent(cmd)
			}
        break
	}
    
    updateStatus()

	if ( result[0] != null ) { result }
}

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand([0x31: 5, 0x30: 2, 0x84: 1])
	state.sec = 1
	if (encapsulatedCommand) {
		zwaveEvent(encapsulatedCommand)
	} else {
		log.warn "Unable to extract encapsulated cmd from $cmd"
		createEvent(descriptionText: cmd.toString())
	}
}

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityCommandsSupportedReport cmd) {
	response(configure())
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
    if (cmd.parameterNumber.toInteger() == 81 && cmd.configurationValue == [255]) {
        update_current_properties([parameterNumber: "81", configurationValue: [1]])
        log.debug "${device.displayName} parameter '${cmd.parameterNumber}' with a byte size of '${cmd.size}' is set to '1'"
    } else {
        update_current_properties(cmd)
        log.debug "${device.displayName} parameter '${cmd.parameterNumber}' with a byte size of '${cmd.size}' is set to '${cmd2Integer(cmd.configurationValue)}'"
    }
}

def zwaveEvent(physicalgraph.zwave.commands.wakeupv1.WakeUpIntervalReport cmd)
{
	log.debug "WakeUpIntervalReport ${cmd.toString()}"
    state.wakeInterval = cmd.seconds
}

def zwaveEvent(physicalgraph.zwave.commands.batteryv1.BatteryReport cmd) {
    log.debug "Battery Report: $cmd"
	def map = [ name: "battery", unit: "%" ]
	if (cmd.batteryLevel == 0xFF) {
		map.value = 1
		map.descriptionText = "${device.displayName} battery is low"
		map.isStateChange = true
	} else {
		map.value = cmd.batteryLevel
	}
    state.lastBatteryReport = now()
	createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd)
{
	def map = [:]
	switch (cmd.sensorType) {
		case 1:
			map.name = "temperature"
			def cmdScale = cmd.scale == 1 ? "F" : "C"
            state.realTemperature = convertTemperatureIfNeeded(cmd.scaledSensorValue, cmdScale, cmd.precision)
			map.value = getAdjustedTemp(state.realTemperature)
			map.unit = getTemperatureScale()
			break;
		case 3:
			map.name = "illuminance"
            state.realLuminance = cmd.scaledSensorValue.toInteger()
			map.value = getAdjustedLuminance(cmd.scaledSensorValue.toInteger())
			map.unit = "lux"
			break;
        case 5:
			map.name = "humidity"
            state.realHumidity = cmd.scaledSensorValue.toInteger()
			map.value = getAdjustedHumidity(cmd.scaledSensorValue.toInteger())
			map.unit = "%"
			break;
		case 27:
        	map.name = "ultravioletIndex"
            state.realUV = cmd.scaledSensorValue.toInteger()
            map.value = getAdjustedUV(cmd.scaledSensorValue.toInteger())
            map.unit = ""
            break;
		default:
			map.descriptionText = cmd.toString()
	}
	createEvent(map)
}

def motionEvent(value) {
	def map = [name: "motion"]
	if (value) {
		map.value = "active"
		map.descriptionText = "$device.displayName detected motion"
	} else {
		map.value = "inactive"
		map.descriptionText = "$device.displayName motion has stopped"
	}
	createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.commands.sensorbinaryv2.SensorBinaryReport cmd) {
	setConfigured()
	motionEvent(cmd.sensorValue)
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
	motionEvent(cmd.value)
}

def zwaveEvent(physicalgraph.zwave.commands.notificationv3.NotificationReport cmd) {
	def result = []
	if (cmd.notificationType == 7) {
		switch (cmd.event) {
			case 0:
				result << motionEvent(0)
				result << createEvent(name: "tamper", value: "clear", descriptionText: "$device.displayName tamper cleared")
                result << createEvent(name: "acceleration", value: "inactive", descriptionText: "$device.displayName tamper cleared")
				break
			case 3:
				result << createEvent(name: "tamper", value: "detected", descriptionText: "$device.displayName was moved")
                result << createEvent(name: "acceleration", value: "active", descriptionText: "$device.displayName was moved")
				break
			case 7:
				result << motionEvent(1)
				break
		}
	} else {
		result << createEvent(descriptionText: cmd.toString(), isStateChange: false)
	}
	result
}

def zwaveEvent(physicalgraph.zwave.commands.wakeupv1.WakeUpNotification cmd)
{
    log.debug "Device ${device.displayName} woke up"

    def request = sync_properties()
    
    if (!state.lastBatteryReport || (now() - state.lastBatteryReport) / 60000 >= 60 * 24)
    {
        log.debug "Over 24hr since last battery report. Requesting report"
        request << zwave.batteryV1.batteryGet()
    }

    if(request != []){
       response(commands(request) + ["delay 5000", zwave.wakeUpV1.wakeUpNoMoreInformation().format()])
    } else {
       log.debug "No commands to send"
       response([zwave.wakeUpV1.wakeUpNoMoreInformation().format()])
    }
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd){
    log.debug "Firmware Report ${cmd.toString()}"
    def firmwareVersion
    switch(cmd.checksum){
       case "35235":
          firmwareVersion = "1.06"
       break;
       case "65307":
          firmwareVersion = "1.07"
       break;
       case "42699":
          firmwareVersion = "1.08"
       break;
       default:
          firmwareVersion = cmd.checksum
    }
    updateDataValue("firmware", firmwareVersion)
    createEvent(name: "currentFirmware", value: firmwareVersion)
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
    log.debug "Unknown Z-Wave Command: ${cmd.toString()}"
}

def refresh() {
   	log.debug "Aeon Multisensor 6 refresh()"

    def request = []
    if (state.lastRefresh != null && now() - state.lastRefresh < 5000) {
        log.debug "Refresh Double Press"
        def configuration = parseXml(configuration_model())
        configuration.Value.each
        {
            if ( "${it.@setting_type}" == "zwave" ) {
                request << zwave.configurationV1.configurationGet(parameterNumber: "${it.@index}".toInteger())
            }
        } 
        request << zwave.firmwareUpdateMdV2.firmwareMdGet()
        request << zwave.wakeUpV1.wakeUpIntervalGet()
    }
    state.lastRefresh = now()
    request << zwave.batteryV1.batteryGet()
    request << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:1, scale:1)
    request << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:3, scale:1)
    request << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:5, scale:1)
    request << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:27, scale:1)
    commands(request)
}

def configure() {
    log.debug "Configuring Device For SmartThings Use"
    def cmds = []

    cmds += sync_properties()
    commands(cmds)
}

def updated()
{
    log.debug "updated() is being called"
    if(settings."101" != null && settings."101" == "240") sendEvent(name:"battery", value: "USB Powered 100")
    else sendEvent(name:"battery", value: "Waiting for report ?")
    ["201", "202", "203", "204"].each { i ->
        if(settings.i == null)
            update_current_properties([parameterNumber: i, configurationValue: ["0"]])
        else
            update_current_properties([parameterNumber: i, configurationValue: [settings.i]])
    }
    
    if (state.realTemperature != null) sendEvent(name:"temperature", value: getAdjustedTemp(state.realTemperature))
    if (state.realHumidity != null) sendEvent(name:"humidity", value: getAdjustedHumidity(state.realHumidity))
    if (state.realLuminance != null) sendEvent(name:"illuminance", value: getAdjustedLuminance(state.realLuminance))
    if (state.realUV != null) sendEvent(name:"ultravioletIndex", value: getAdjustedUV(state.realUV))
    
    updateStatus()
    
    update_needed_settings()
    
    sendEvent(name:"needUpdate", value: device.currentValue("needUpdate"), displayed:false, isStateChange: true)
}

def sync_properties()
{   
    def currentProperties = state.currentProperties ?: [:]
    def configuration = parseXml(configuration_model())

    def cmds = []
    
    if(!state.firmware || state.firmware == ""){
       log.debug "Requesting device firmware version"
       cmds << zwave.firmwareUpdateMdV2.firmwareMdGet()
    }
   
    configuration.Value.each
    {
        if ( "${it.@setting_type}" == "zwave" ) {
            if (! currentProperties."${it.@index}" || currentProperties."${it.@index}" == null)
            {
                if(it.@index == "81"){
                    if(device.currentValue("currentFirmware") == "1.08"){
                        log.debug "Looking for current value of parameter ${it.@index}"
                        cmds << zwave.configurationV1.configurationGet(parameterNumber: it.@index.toInteger())
                    }
                } else {
                    log.debug "Looking for current value of parameter ${it.@index}"
                    cmds << zwave.configurationV1.configurationGet(parameterNumber: it.@index.toInteger())
                }
            }
        }
    }
    
    if (device.currentValue("needUpdate") == "YES") { cmds += update_needed_settings() }
    return cmds
}

def convertParam(number, value) {
	switch (number){
    	case 201:
        	if (value < 0)
            	256 + value
        	else if (value > 100)
            	value - 256
            else
            	value
        break
        case 202:
        	if (value < 0)
            	256 + value
        	else if (value > 100)
            	value - 256
            else
            	value
        break
        case 203:
            if (value < 0)
            	65536 + value
        	else if (value > 1000)
            	value - 65536
            else
            	value
        break
        case 204:
        	if (value < 0)
            	256 + value
        	else if (value > 100)
            	value - 256
            else
            	value
        break
        default:
        	value
        break
    }
}

def update_current_properties(cmd)
{
    def currentProperties = state.currentProperties ?: [:]
    def convertedConfigurationValue = convertParam("${cmd.parameterNumber}".toInteger(), cmd2Integer(cmd.configurationValue))
    currentProperties."${cmd.parameterNumber}" = cmd.configurationValue

    if (settings."${cmd.parameterNumber}" != null)
    {
        if (settings."${cmd.parameterNumber}".toInteger() == convertedConfigurationValue)
        {
            sendEvent(name:"needUpdate", value:"NO", displayed:false, isStateChange: true)
        }
        else
        {
            sendEvent(name:"needUpdate", value:"YES", displayed:false, isStateChange: true)
        }
    }

    state.currentProperties = currentProperties
}

def update_needed_settings()
{
    def cmds = []
    def currentProperties = state.currentProperties ?: [:]
     
    def configuration = parseXml(configuration_model())
    def isUpdateNeeded = "NO"

    if(state.wakeInterval == null || state.wakeInterval != getAdjustedWake()){
        isUpdateNeeded = "YES"
        log.debug "Setting Wake Interval to ${getAdjustedWake()}"
        cmds <<zwave.wakeUpV1.wakeUpIntervalSet(seconds: getAdjustedWake(), nodeid:zwaveHubNodeId)
        cmds << zwave.wakeUpV1.wakeUpIntervalGet()
    }
   
    configuration.Value.each
    {     
        if ("${it.@setting_type}" == "zwave"){
            if (currentProperties."${it.@index}" == null && it.@index == "81")
            {
                if(device.currentValue("currentFirmware") == "1.08"){
                    log.debug "Current value of parameter ${it.@index} is unknown"
                    isUpdateNeeded = "YES"
                }
            }
            else if (currentProperties."${it.@index}" == null)
            {
                log.debug "Current value of parameter ${it.@index} is unknown"
                isUpdateNeeded = "YES"
            }
            else if (settings."${it.@index}" != null && convertParam(it.@index.toInteger(), cmd2Integer(currentProperties."${it.@index}")) != settings."${it.@index}".toInteger() && it.@index != "201" && it.@index != "202" && it.@index != "203" && it.@index != "204" && it.@index != "111")
            { 
                isUpdateNeeded = "YES"

                log.debug "Parameter ${it.@index} will be updated to " + settings."${it.@index}"
                def convertedConfigurationValue = convertParam(it.@index.toInteger(), settings."${it.@index}".toInteger())
                cmds << zwave.configurationV1.configurationSet(configurationValue: integer2Cmd(convertedConfigurationValue, it.@byteSize.toInteger()), parameterNumber: it.@index.toInteger(), size: it.@byteSize.toInteger())
                cmds << zwave.configurationV1.configurationGet(parameterNumber: it.@index.toInteger())
            } 
            else if (settings."${it.@index}" != null && it.@index == "111" && convertParam(it.@index.toInteger(), cmd2Integer(currentProperties."${it.@index}")) != getRoundedInterval(settings."111".toInteger()))
            {   
                isUpdateNeeded = "YES"

                log.debug "Parameter ${it.@index} will be updated to " + getRoundedInterval(settings."${it.@index}")
                cmds << zwave.configurationV1.configurationSet(configurationValue: integer2Cmd(getRoundedInterval(settings."111".toInteger()), 4), parameterNumber: it.@index.toInteger(), size: 4)
                cmds << zwave.configurationV1.configurationGet(parameterNumber: it.@index.toInteger())
            }
        }
    }
    sendEvent(name:"needUpdate", value: isUpdateNeeded, displayed:false, isStateChange: true)
    return cmds
}

/**
* Convert 1 and 2 bytes values to integer
*/
def cmd2Integer(array) { 
switch(array.size()) {
	case 1:
		array[0]
    break
	case 2:
    	((array[0] & 0xFF) << 8) | (array[1] & 0xFF)
    break
	case 4:
    	((array[0] & 0xFF) << 24) | ((array[1] & 0xFF) << 16) | ((array[2] & 0xFF) << 8) | (array[3] & 0xFF)
	break
}
}

def integer2Cmd(value, size) {
	switch(size) {
	case 1:
		[value]
    break
	case 2:
    	def short value1   = value & 0xFF
        def short value2 = (value >> 8) & 0xFF
        [value2, value1]
    break
	case 4:
    	def short value1 = value & 0xFF
        def short value2 = (value >> 8) & 0xFF
        def short value3 = (value >> 16) & 0xFF
        def short value4 = (value >> 24) & 0xFF
		[value4, value3, value2, value1]
	break
	}
}

private setConfigured() {
	updateDataValue("configured", "true")
}

private isConfigured() {
	getDataValue("configured") == "true"
}

private command(physicalgraph.zwave.Command cmd) {
    
	if (state.sec && cmd.toString() != "WakeUpIntervalGet()") {
		zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	} else {
		cmd.format()
	}
}

private commands(commands, delay=1000) {
	delayBetween(commands.collect{ command(it) }, delay)
}

def generate_preferences(configuration_model)
{
    def configuration = parseXml(configuration_model)
    configuration.Value.each
    {
        switch(it.@type)
        {
            case ["byte","short","four"]:
                input "${it.@index}", "number",
                    title:"${it.@index} - ${it.@label}\n" + "${it.Help}",
                    range: "${it.@min}..${it.@max}",
                    defaultValue: "${it.@value}"
            break
            case "list":
                def items = []
                it.Item.each { items << ["${it.@value}":"${it.@label}"] }
                input "${it.@index}", "enum",
                    title:"${it.@index} - ${it.@label}\n" + "${it.Help}",
                    defaultValue: "${it.@value}",
                    options: items
            break
            case "decimal":
               input "${it.@index}", "decimal",
                    title:"${it.@index} - ${it.@label}\n" + "${it.Help}",
                    //range: "${it.@min}..${it.@max}",
                    defaultValue: "${it.@value}"
            break
            case "boolean":
               input "${it.@index}", "boolean",
                    title:"${it.@label}\n" + "${it.Help}",
                    //range: "${it.@min}..${it.@max}",
                    defaultValue: "${it.@value}"
            break
        }
    }
}

private getBatteryRuntime() {
   def currentmillis = now() - state.batteryRuntimeStart
   def days=0
   def hours=0
   def mins=0
   def secs=0
   secs = (currentmillis/1000).toInteger() 
   mins=(secs/60).toInteger() 
   hours=(mins/60).toInteger() 
   days=(hours/24).toInteger() 
   secs=(secs-(mins*60)).toString().padLeft(2, '0') 
   mins=(mins-(hours*60)).toString().padLeft(2, '0') 
   hours=(hours-(days*24)).toString().padLeft(2, '0') 
 

  if (days>0) { 
      return "$days days and $hours:$mins:$secs"
  } else {
      return "$hours:$mins:$secs"
  }
}

private getRoundedInterval(number) {
    double tempDouble = (number / 60)
    if (tempDouble == tempDouble.round())
       return (tempDouble * 60).toInteger()
    else 
       return ((tempDouble.round() + 1) * 60).toInteger()
}

private getAdjustedWake(){
    def wakeValue
    if (device.currentValue("currentFirmware") != null && settings."101" != null && settings."111" != null){
        if (device.currentValue("currentFirmware") == "1.08"){
            if (settings."101".toInteger() == 241){   
                if (settings."111".toInteger() <= 3600){
                    wakeValue = getRoundedInterval(settings."111")
                } else {
                    wakeValue = 3600
                }
            } else {
                wakeValue = 1800
            }
        } else {
            if (settings."101".toInteger() == 241){   
                if (settings."111".toInteger() <= 3600){
                    wakeValue = getRoundedInterval(settings."111")
                } else {
                    wakeValue = getRoundedInterval(settings."111".toInteger() % 2)
                }
            } else {
                wakeValue = 240
            }
        }
    } else {
        wakeValue = 3600
    }
    return wakeValue.toInteger()
}

private getAdjustedTemp(value) {
    
    value = Math.round((value as Double) * 100) / 100

	if (settings."201") {
	   return value =  value + Math.round(settings."201" * 100) /100
	} else {
       return value
    }
    
}

private getAdjustedHumidity(value) {
    
    value = Math.round((value as Double) * 100) / 100

	if (settings."202") {
	   return value =  value + Math.round(settings."202" * 100) /100
	} else {
       return value
    }
    
}

private getAdjustedLuminance(value) {
    
    value = Math.round((value as Double) * 100) / 100

	if (settings."203") {
	   return value =  value + Math.round(settings."203" * 100) /100
	} else {
       return value
    }
    
}

private getAdjustedUV(value) {
    
    value = Math.round((value as Double) * 100) / 100

	if (settings."204") {
	   return value =  value + Math.round(settings."204" * 100) /100
	} else {
       return value
    }
    
}

def resetBatteryRuntime() {
    if (state.lastReset != null && now() - state.lastReset < 5000) {
        log.debug "Reset Double Press"
        state.batteryRuntimeStart = now()
        updateStatus()
    }
    state.lastReset = now()
}

private updateStatus(){
   def result = []
   if(state.batteryRuntimeStart != null){
        sendEvent(name:"batteryRuntime", value:getBatteryRuntime(), displayed:false)
        if (device.currentValue('currentFirmware') != null){
            sendEvent(name:"statusText2", value: "Firmware: v${device.currentValue('currentFirmware')} - Battery: ${getBatteryRuntime()} Double tap to reset", displayed:false)
        } else {
            sendEvent(name:"statusText2", value: "Battery: ${getBatteryRuntime()} Double tap to reset", displayed:false)
        }
    } else {
        state.batteryRuntimeStart = now()
    }

    String statusText = ""
    if(device.currentValue('humidity') != null)
        statusText = "RH ${device.currentValue('humidity')}% - "
    if(device.currentValue('illuminance') != null)
        statusText = statusText + "LUX ${device.currentValue('illuminance')} - "
    if(device.currentValue('ultravioletIndex') != null)
        statusText = statusText + "UV ${device.currentValue('ultravioletIndex')} - "
        
    if (statusText != ""){
        statusText = statusText.substring(0, statusText.length() - 2)
        sendEvent(name:"statusText", value: statusText, displayed:false)
    }
}

def configuration_model()
{
'''
<configuration>
    <Value type="list" index="101" label="Battery or USB?" min="240" max="241" value="241" byteSize="4">
    <Help>
Is the device powered by battery or usb?
    </Help>
        <Item label="Battery" value="241" />
        <Item label="USB" value="240" />
  </Value>
  <Value type="list" index="40" label="Enable selective reporting?" min="0" max="1" value="0" byteSize="1" setting_type="zwave">
    <Help>
Enable/disable the selective reporting only when measurements reach a certain threshold or percentage set in 41-44 below. This is used to reduce network traffic.
Default: No (Recommended for USB/Battery)
    </Help>
        <Item label="No" value="0" />
        <Item label="Yes" value="1" />
  </Value>
  <Value type="short" byteSize="2" index="3" label="PIR reset time" min="10" max="3600" value="20" setting_type="zwave">
    <Help>
Number of seconds to wait to report motion cleared after a motion event if there is no motion detected.
Range: 10~3600.
Default: 240 (4 minutes)
Note:
(1), The time unit is seconds if the value range is in 10 to 255.
(2), If the value range is in 256 to 3600, the time unit will be minute and its value should follow the below rules:
a), Interval time =Value/60, if the interval time can be divided by 60 and without remainder.
b), Interval time= (Value/60) +1, if the interval time can be divided by 60 and has remainder.
    </Help>
  </Value>
    <Value type="byte" byteSize="1" index="4" label="PIR motion sensitivity" min="0" max="5" value="" setting_type="zwave">
    <Help>
A value from 0-5, from disabled to high sensitivity
Range: 0~5
Default: 5
    </Help>
  </Value>
    <Value type="list" index="81" label="Disable LED?" min="0" max="1" value="0" byteSize="1" setting_type="zwave">
    <Help>
Disable/Enable LED function. (Works on Firmware v1.08 only)
Default: Enabled
    </Help>
        <Item label="No" value="0" />
        <Item label="Yes" value="1" />
  </Value>
    <Value type="byte" byteSize="4" index="111" label="Reporting Interval" min="5" max="2678400" value="" setting_type="zwave">
    <Help>
The interval time of sending reports in Report group 1
Range: 5~
Default: 3600 seconds
Note:
1. The unit of interval time is second if USB power.
2. If battery power, the minimum interval time is 60 minutes by default, for example, if the value is set to be more than 5 and less than 3600, the interval time is 60 minutes, if the value is set to be more than 3600 and less than 7200, the interval time is 120 minutes. You can also change the minimum interval time to 4 minutes via setting the interval value(3 bytes) to 240 in Wake Up Interval Set CC
    </Help>
  </Value>
  <Value type="decimal" byteSize="1" index="201" label="Temperature offset" min="-10" max="10" value="">
    <Help>
Range: -10~10
Default: 0
Note: 
1. The calibration value = standard value - measure value.
E.g. If measure value =85.3F and the standard value = 83.2F, so the calibration value = 83.2F - 85.3F = -2.1F.
If the measure value =60.1F and the standard value = 63.2F, so the calibration value = 63.2F - 60.1℃ = 3.1F. 
    </Help>
  </Value>
  <Value type="decimal" byteSize="1" index="202" label="Humidity offset" min="-50" max="50" value="">
    <Help>
Range: -50~50
Default: 0
Note:
The calibration value = standard value - measure value.
E.g. If measure value = 80RH and the standard value = 75RH, so the calibration value = 75RH – 80RH = -5RH.
If the measure value = 85RH and the standard value = 90RH, so the calibration value = 90RH – 85RH = 5RH. 
    </Help>
  </Value>
    <Value type="decimal" byteSize="2" index="203" label="Luminance offset" min="-1000" max="1000" value="">
    <Help>
Range: -1000~1000
Default: 0
Note:
The calibration value = standard value - measure value.
E.g. If measure value = 800Lux and the standard value = 750Lux, so the calibration value = 750 – 800 = -50.
If the measure value = 850Lux and the standard value = 900Lux, so the calibration value = 900 – 850 = 50.
    </Help>
  </Value>
    <Value type="decimal" byteSize="1" index="204" label="Ultraviolet offset" min="-10" max="10" value="">
    <Help>
Range: -10~10
Default: 0
Note:
The calibration value = standard value - measure value.
E.g. If measure value = 9 and the standard value = 8, so the calibration value = 8 – 9 = -1.
If the measure value = 7 and the standard value = 9, so the calibration value = 9 – 7 = 2. 
    </Help>
  </Value>
</configuration>
'''
}