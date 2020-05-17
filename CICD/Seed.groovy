def script = makeJobs()
print '\n'*5+'-'*80+'\n\t\t\tSCRIPT\n'
print script
print '\n'*5+'-'*80+'\n\t\t\tEND\n'
node{
	jobDsl scriptText: script
}

@NonCPS
def makeJobs(){
	def modules = ['mrchecker-core-module','mrchecker-database-module','mrchecker-example-module','mrchecker-mobile-module','mrchecker-security-module','mrchecker-selenium-module','mrchecker-webapi-module']
	def repo_url = 'https://github.com/other019/mrchecker-source.git'
	def jobTemplateText = """
	multibranchPipelineJob("\${module}"){
		description("Build source code and provide packages")
		branchSources{
			git{
				id('12314')
				remote('${repo_url}')
			}
		}
		orphanedItemStrategy{
			discardOldItems{
				daysToKeep(30)
			}
		}
		factory{
			workflowBranchProjectFactory {
				scriptPath('CICD/Jenkinsfile')
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
	modules.each{
		script += jobTemplate.make([module:it])
	}
	return script
}
