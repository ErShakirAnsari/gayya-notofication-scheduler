package org.ajaxer.service;

import lombok.RequiredArgsConstructor;
import org.ajaxer.common.Constant;
import org.springframework.stereotype.Service;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */
@Service
@RequiredArgsConstructor
public class CommonService
{
	private final EnvironmentService environmentService;

	public String getENV()
	{
		String envMode = environmentService.getEnvMode();
		if (envMode == null)
			throw new IllegalStateException("envMode [DEV/PROD] environment variable is not set");

		if (envMode.equals(Constant.ENV_DEV) || envMode.equals(Constant.ENV_PROD))
			return envMode;

		throw new IllegalStateException("envMode [DEV/PROD] environment variable is not set");
	}

	public String getCollectionPrefix()
	{
		return getENV().equalsIgnoreCase(Constant.ENV_DEV)
				? "test_"
				: "";

	}

	public String getPrefixedCollectionName(String collectionName)
	{
		return getCollectionPrefix() + collectionName;
	}
}
