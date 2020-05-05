
node{
	jobDsl scriptText: """
	def repo_url = 'https://github.com/other019/mrchecker-source.git'
	multibranchPipelineJob('build'){
		description("Build source code and provide packages")
		branchSources{
			git{
				id('12314')
				remote(repo_url)
			}
			scriptPath('CICD/Jenkinsfile')
			//github is a valid option too
		}
		orphanedItemStrategy{
			discardOldItems{
				daysToKeep(30)
			}
		}
		triggers{
			cron("")
		}
	}
	"""
}