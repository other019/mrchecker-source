class Config{
    def jenkinsFolder;
    def scriptPath;
    def repoUrl;
    def module;
}
def repoUrl = 'https://github.com/other019/mrchecker-source.git'
def modules = ['mrchecker-core-module','mrchecker-database-module','mrchecker-example-module','mrchecker-mobile-module','mrchecker-security-module','mrchecker-selenium-module','mrchecker-webapi-module']
def configs = []
def folders = ['build','test','deploy']
modules.each{
    configs << new Config(jenkinsFolder:'build', scriptPath:'CICD/Jenkinsfile', repoUrl: repoUrl, module:it)
    configs << new Config(jenkinsFolder:'test' , scriptPath:"mrchecker-framework-modules/${it}/Jenkinsfile", repoUrl: repoUrl, module:it)
    configs << new Config(jenkinsFolder:'deploy', scriptPath:'CICD/Deploy_Jenkinsfile', repoUrl: repoUrl, module:it)
}
def script = makeJobs(configs,folders)

print '\n'*5+'-'*80+'\n\t\t\tSCRIPT\n'
print script
print '\n'*5+'-'*80+'\n\t\t\tEND\n'
node{
	jobDsl scriptText: script
}

@NonCPS
def makeJobs(configs,folders){
	def jobTemplateText = """
	multibranchPipelineJob("\${it.jenkinsFolder}/\${it.module}"){
		description("Build source code and provide packages")
		branchSources{
			git{
				id('12314')
				remote('\${it.repoUrl}')
			}
		}
		orphanedItemStrategy{
			discardOldItems{
				daysToKeep(30)
			}
		}
		factory{
			workflowBranchProjectFactory {
				scriptPath('\${it.scriptPath}')
			}
		}
		triggers{
			cron("")
		}
	}
	"""
	def script = ''
	def engine = new groovy.text.SimpleTemplateEngine()
	def jobTemplate = engine.createTemplate(jobTemplateText)
    folders.each{
        script += "folder('${it}')\n"
    }
	configs.each{
		script += jobTemplate.make([it:it])
	}
	return script
}
