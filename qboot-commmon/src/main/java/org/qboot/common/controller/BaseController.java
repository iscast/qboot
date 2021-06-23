package org.qboot.common.controller;

import org.qboot.common.entity.ResponeModel;
import org.qboot.common.exception.errorcode.SystemErrTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseController
 * @author iscast
 * @date 2020-09-25
 */
public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected ResponeModel ok(){ return ResponeModel.ok(SystemErrTable.SUCCESS); }
	protected ResponeModel err(){ return ResponeModel.error(SystemErrTable.ERR); }
}