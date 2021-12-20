package pl.radomiej.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.radomiej.search.domains.ImportResult;
import pl.radomiej.search.services.imports.ImporterService;
import pl.radomiej.search.services.imports.PostalcodesFixService;

@RestController
@RequestMapping("/import")
public class ImportController {
	
	@Autowired
	private ImporterService importerService;

	@Autowired
	private PostalcodesFixService postalcodesFixService;
	
	@RequestMapping("/fromFile")
	public ImportResult importAddresses(@RequestParam(value = "path") String path) {
		if(path == null || path.isEmpty()) return new ImportResult();
		return importerService.importAddresses(path);
	}

	@RequestMapping("/fixBadPostals")
	public ImportResult importAddresses() {
		postalcodesFixService.fixPostalcodes();
		return new ImportResult();
	}
}
