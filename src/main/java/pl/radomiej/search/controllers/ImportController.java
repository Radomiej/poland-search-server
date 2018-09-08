package pl.radomiej.search.controllers;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.radomiej.search.domains.ImportResult;
import pl.radomiej.search.services.ImporterService;

@RestController
@RequestMapping("/import")
public class ImportController {
	
	@Autowired
	private ImporterService importerService;
	
	@RequestMapping("")
	public ImportResult importAddresses(@QueryParam(value = "path") String path) {
		return importerService.importAddresses(path);
	}
}
